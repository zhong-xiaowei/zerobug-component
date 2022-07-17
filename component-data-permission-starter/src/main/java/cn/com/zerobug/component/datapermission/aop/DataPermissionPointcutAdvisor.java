package cn.com.zerobug.component.datapermission.aop;

import cn.com.zerobug.component.datapermission.annotation.DataPermission;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author zhongxiaowei
 * @date 2022/2/28
 */
public class DataPermissionPointcutAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;
    private final Pointcut pointcut;
    private DataPermissionMethodInterceptor dataPermissionMethodInterceptor;

    public DataPermissionPointcutAdvisor(DataPermissionMethodInterceptor dataPermissionMethodInterceptor) {
        this.dataPermissionMethodInterceptor = dataPermissionMethodInterceptor;
        this.advice = buildAdvice();
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    protected Advice buildAdvice() {
        return this.dataPermissionMethodInterceptor;
    }

    protected Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(DataPermission.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DataPermission.class);
        ComposablePointcut result = new ComposablePointcut(cpc).union(mpc);
        return result;
    }

}
