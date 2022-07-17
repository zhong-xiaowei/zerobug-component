package cn.com.zerobug.component.filter.chain.internal;

import cn.hutool.core.util.StrUtil;
import cn.com.zerobug.component.filter.exception.IllegalRequestException;
import cn.com.zerobug.component.filter.chain.wrapper.RepeatedlyRequestWrapper;
import cn.com.zerobug.component.filter.annotation.RepeatSubmit;
import cn.com.zerobug.component.filter.chain.matcher.MvcRequestMatcher;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhongxiaowei
 * @date 2021/12/27
 */
@Order(0)
public class RepeatSubmitInternalFilter extends BaseInternalFilter {

    private static final String REPEAT_SUBMIT_LOCL_CACHE_PREFIX = "repeat_submit:lock:";

    public RepeatSubmitInternalFilter(
            StringRedisTemplate stringRedisTemplate,
            HandlerMappingIntrospector handlerMappingIntrospector) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.handlerMappingIntrospector = handlerMappingIntrospector;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        RepeatedlyRequestWrapper repeatedlyWrapper = getRepeatedlyWrapper(request);
        MvcRequestMatcher        matcher           = new MvcRequestMatcher(handlerMappingIntrospector);
        try {
            RepeatSubmit repeatSubmit = matcher.getMethodAnnotation(request, RepeatSubmit.class);
            if (repeatSubmit != null) {
                JSONObject data;
                if (repeatedlyWrapper == null || HttpMethod.GET.matches(request.getMethod())) {
                    String jsonStr = JSONObject.toJSONString(request.getParameterMap());
                    data = StrUtil.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr) : new JSONObject();
                } else {
                    if (repeatedlyWrapper != null) {
                        String body = repeatedlyWrapper.getBody();
                        data = JSON.parseObject(body);
                    }
                    throw new IllegalRequestException("不支持的请求！");
                }
                doRepeatVerify(request, repeatSubmit, data);
            }
        } catch (IllegalRequestException e) {
            rejectHandler.afterReject(request, response, e);
            return;
        } catch (Exception e) {
        }
        chain.doFilter(request, response);
    }

    private void doRepeatVerify(HttpServletRequest request, RepeatSubmit repeatSubmit, JSONObject data) {
        String identify = request.getHeader(repeatSubmit.identify());
        if (StrUtil.isEmpty(identify)) {
            identify = request.getRequestURI();
        }
        StringBuilder sb = new StringBuilder(identify);
        data.values().forEach(v -> sb.append(v));
        String key      = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        String cacheKey = REPEAT_SUBMIT_LOCL_CACHE_PREFIX + key;
        Boolean suceed = stringRedisTemplate
                .opsForValue()
                .setIfAbsent(cacheKey, "1", repeatSubmit.interval(), TimeUnit.MILLISECONDS);
        if (!suceed) {
            throw new IllegalRequestException(repeatSubmit.message());
        }
    }

}
