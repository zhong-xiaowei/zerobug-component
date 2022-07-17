package cn.com.zerobug.component.filter.annotation;


import java.lang.annotation.*;

/**
 * @author zhongxiaowei
 * @date 2021/12/22
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterIgnore {

    IgnoreType type() default IgnoreType.ALL;

}
