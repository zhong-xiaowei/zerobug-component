package cn.com.zerobug.component.datapermission.annotation;

import java.lang.annotation.*;

/**
 * @author zhongxiaowei
 * @date 2022/3/3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface DataPermissionExclude {

    /**
     * 排除的 Mapper 类
     * @return
     */
    Class clazz();

    /**
     * 排除的 mapper 类的方法
     * @return
     */
    String method();

}
