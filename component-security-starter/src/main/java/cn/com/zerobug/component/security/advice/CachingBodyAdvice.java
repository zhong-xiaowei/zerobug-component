package cn.com.zerobug.component.security.advice;

import org.springframework.core.MethodClassKey;
import org.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public class CachingBodyAdvice extends AbstractBodyAdvice {

    private static final Annotation NULL_ANNOTATION = () -> null;
    Map<MethodClassKey, Annotation> annotationCache = new HashMap<>(64);

    public <A extends Annotation> A getAnnotationFromCache(MethodParameter methodParameter, Class<A> annotationClass) {
        MethodClassKey methodClassKey = new MethodClassKey(methodParameter.getMethod(), methodParameter.getMethod().getClass());
        Annotation annotation = annotationCache.get(methodClassKey);
        if (annotation != null) {
            if (annotation.equals(NULL_ANNOTATION)) {
                return null;
            }
            return (A) annotation;
        }
        annotation = super.getAnnotationFromMethodAndType(methodParameter, annotationClass);
        annotationCache.putIfAbsent(methodClassKey, annotation);
        return (A) annotation;
    }


}
