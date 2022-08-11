package cn.com.zerobug.component.security.advice;

import org.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;

/**
 * @author zhongxiaowei
 * @date 2022/1/19
 */
public abstract class AbstractBodyAdvice {

    public <A extends Annotation> A getAnnotationFromMethodAndType(MethodParameter methodParameter, Class<A> annotationClass) {
        return getAnnotation(methodParameter, annotationClass, true);
    }

    public <A extends Annotation> A getAnnotation(MethodParameter methodParameter, Class<A> annotationClass, boolean lookType) {
        A annotation = methodParameter.getMethodAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        if (lookType) {
            annotation = methodParameter.getMethod().getDeclaringClass().getAnnotation(annotationClass);
        }
        return annotation;
    }

}
