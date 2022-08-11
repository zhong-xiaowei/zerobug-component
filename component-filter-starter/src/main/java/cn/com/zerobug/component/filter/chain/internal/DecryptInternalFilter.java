package cn.com.zerobug.component.filter.chain.internal;

import cn.com.zerobug.component.filter.chain.matcher.MvcRequestMatcher;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.com.zerobug.component.filter.annotation.Encrypt;
import cn.com.zerobug.component.filter.annotation.EncryptStrategy;
import cn.com.zerobug.component.filter.chain.wrapper.RepeatedlyRequestWrapper;
import cn.com.zerobug.component.filter.encrypt.Encryptor;
import cn.com.zerobug.component.filter.encrypt.EncryptorFactory;
import cn.com.zerobug.component.filter.exception.IllegalRequestException;
import cn.com.zerobug.component.filter.properties.EncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解密过滤器
 *
 * @author zhongxiaowei
 * @date 2022/1/19
 */
@Order(-1)
@Slf4j
public class DecryptInternalFilter extends BaseInternalFilter {

    private EncryptProperties          properties;
    private HandlerMappingIntrospector handlerMappingIntrospector;

    public DecryptInternalFilter(EncryptProperties properties, HandlerMappingIntrospector handlerMappingIntrospector) {
        this.properties = properties;
        this.handlerMappingIntrospector = handlerMappingIntrospector;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!checkIfEnable(request)) {
            chain.doFilter(request, response);
        }else {
            try {
                RepeatedlyRequestWrapper wrapper = decryptHandle(request);
                chain.doFilter(wrapper, response);
            } catch (IllegalRequestException e) {
                rejectHandler.afterReject(request, response, e);
            }
        }
    }

    private boolean checkIfEnable(HttpServletRequest request) {
        if (!properties.getEnable()) {
            return false;
        }
        try {
            Encrypt annotation = new MvcRequestMatcher(handlerMappingIntrospector)
                    .getMethodAnnotation(request, Encrypt.class);
            if (annotation != null) {
                boolean         open     = annotation.open();
                EncryptStrategy strategy = annotation.strategy();
                return open && !EncryptStrategy.RESPONSE_ENCRYPT.equals(strategy);
            }
        } catch (Exception e) {
            log.error("获取 【Encrypt】注解失败！原因：{}", ExceptionUtil.getMessage(e));
        }
        return false;
    }

    private RepeatedlyRequestWrapper decryptHandle(HttpServletRequest request) {
        String                   ciphertext = null;
        // 当前链路只支持 JSON格式，如果需要其他格式需要重新包装一下进行处理
        RepeatedlyRequestWrapper wrapper    = getRepeatedlyWrapper(request);
        if (wrapper == null) {
            throw new IllegalRequestException("不正确的请求");
        }
        try {
            ciphertext = wrapper.getBody();
            if (StrUtil.isNotEmpty(ciphertext)) {
                Encryptor encryptor        = EncryptorFactory.generate(properties);
                Object    decryptedContent = encryptor.requestDecrypt(ciphertext);
                wrapper.setBody(decryptedContent.toString().getBytes());
            }
        } catch (Exception e) {
            log.error("解密失败！密文：【{}】,  原因：【{}】", ciphertext, ExceptionUtil.getMessage(e));
            throw new IllegalRequestException("验证失败");
        }
        return wrapper;
    }
}
