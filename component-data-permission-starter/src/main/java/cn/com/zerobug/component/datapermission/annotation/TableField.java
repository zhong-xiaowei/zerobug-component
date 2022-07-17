package cn.com.zerobug.component.datapermission.annotation;

import java.lang.annotation.*;

/**
 * @author zhongxiaowei
 * @date 2022/3/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface TableField {

    /**
     * 表别名 例：user as u  此处的别名就应该填写 u
     * @return
     */
    String alias() default "";

    /**
     * 表字段 例如：dept_id
     * @return
     */
    String field();

}

