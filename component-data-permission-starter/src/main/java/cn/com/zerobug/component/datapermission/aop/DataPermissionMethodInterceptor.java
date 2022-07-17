package cn.com.zerobug.component.datapermission.aop;

import cn.com.zerobug.component.datapermission.annotation.DataPermission;
import cn.com.zerobug.component.datapermission.utils.DataPermissionContextHolder;
import lombok.Getter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongxiaowei
 * @date 2022/2/28
 */
public class DataPermissionMethodInterceptor implements MethodInterceptor {

    @Getter
    private final Map<MethodClassKey, DataPermission> dataPermissionCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DataPermission dataPermission = this.findAnnotation(invocation);
        if (dataPermission != null) {
            DataPermissionContextHolder.add(dataPermission);
        }
        try {
            return invocation.proceed();
        } finally {
            if (dataPermission != null) {
                DataPermissionContextHolder.remove();
            }
        }
    }

    private DataPermission findAnnotation(MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        Object targetObject = methodInvocation.getThis();
        Class<?> clazz = targetObject != null ? targetObject.getClass() : method.getDeclaringClass();
        MethodClassKey methodClassKey = new MethodClassKey(method, clazz);
        DataPermission dataPermission = dataPermissionCache.get(methodClassKey);
        if (dataPermission != null) {
            return dataPermission;
        }
        dataPermission = AnnotationUtils.findAnnotation(method, DataPermission.class);
        if (dataPermission == null) {
            dataPermission = AnnotationUtils.findAnnotation(clazz, DataPermission.class);
        }
        if (dataPermission != null) {
            dataPermissionCache.putIfAbsent(methodClassKey, dataPermission);
        }
        return dataPermission;
    }
}
