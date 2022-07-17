package cn.com.zerobug.component.datapermission.annotation;

import cn.com.zerobug.component.datapermission.DataPermissionHandler;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author zhongxiaowei
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataPermission {

    /**
     * 数据权限用到的数据库字段
     *
     * @return
     * @see {@link TableField}
     */
    TableField[] fields();

    /**
     * 排除的mapper方法，主要用作方法内多个mapper方法时不想全部数据过滤时使用
     *
     * @return
     */
    DataPermissionExclude[] exclude() default {};

    /**
     * 禁用某些数据权限的处理器
     *
     * @return
     */
    Class<? extends DataPermissionHandler>[] disable() default {};

    /**
     * 自定义属性，在有需要的时候可以放这里。方便灵活配置。
     *
     * @return
     */
    String[] custom() default {};

}
