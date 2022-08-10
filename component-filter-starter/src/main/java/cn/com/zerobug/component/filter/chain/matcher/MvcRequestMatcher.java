package cn.com.zerobug.component.filter.chain.matcher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@RequiredArgsConstructor
public class MvcRequestMatcher implements RequestMatcher {

    private final HandlerMappingIntrospector mappingIntrospector;

    @Override
    public boolean matches(HttpServletRequest request) throws Exception {
        HandlerMethod method = getHandlerMethod(request);
        if (method != null) {
            return true;
        }
        return false;
    }

    public HandlerMethod getHandlerMethod(HttpServletRequest request) throws Exception {
        MatchableHandlerMapping handlerMapping        = mappingIntrospector.getMatchableHandlerMapping(request);
        HandlerExecutionChain   handlerExecutionChain = handlerMapping.getHandler(request);
        Object                  handler               = handlerExecutionChain.getHandler();
        if (handler instanceof HandlerMethod) {
            return (HandlerMethod) handler;
        }
        return null;
    }

    public <A extends Annotation> A getMethodAnnotation(HttpServletRequest request, Class<A> annotationClass) throws Exception {
        HandlerMethod handlerMethod = getHandlerMethod(request);
        if (handlerMethod == null) {
            return null;
        }
        A annotation = handlerMethod.getMethodAnnotation(annotationClass);
        if (annotation == null){
            // 方法找不到 去类找
            annotation = handlerMethod.getBeanType().getAnnotation(annotationClass);
        }
        return annotation;
    }

}
