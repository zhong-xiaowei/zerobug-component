package cn.com.zerobug.component.log.annotation;

import java.lang.annotation.*;

/**
 * api 接口日志
 *
 * @author zhongxiaowei
 * @date 2022-04-02
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {

    /**
     * 接口作用描述
     *
     * @return
     */
    String description() default "";

    /**
     * 是否保存方法入参
     *
     * @return
     */
    boolean methodArgs() default true;

    /**
     * 是否保存结果数据
     */
    boolean resultData() default true;
}
