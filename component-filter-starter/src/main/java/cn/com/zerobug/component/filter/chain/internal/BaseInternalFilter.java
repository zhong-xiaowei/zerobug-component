package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.CommonRequestRejectHandler;
import cn.com.zerobug.component.filter.chain.RequestRejectHandler;
import cn.com.zerobug.component.filter.chain.wrapper.RepeatedlyRequestWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhongxiaowei
 * @date 2021/12/31
 */
public abstract class BaseInternalFilter extends OncePerRequestFilter {

    protected RequestRejectHandler rejectHandler = new CommonRequestRejectHandler();

    protected HandlerMappingIntrospector handlerMappingIntrospector;
    protected StringRedisTemplate        stringRedisTemplate;

    protected RepeatedlyRequestWrapper getRepeatedlyWrapper(HttpServletRequest request) {
        if (request instanceof RepeatedlyRequestWrapper) {
            return (RepeatedlyRequestWrapper) request;
        }
        return null;
    }

    public void setRejectHandler(RequestRejectHandler rejectHandler) {
        if (rejectHandler != null) {
            this.rejectHandler = rejectHandler;
        }
    }
}
