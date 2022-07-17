package cn.com.zerobug.component.translate.annotation;

import cn.com.zerobug.component.translate.DataTranslator;

import java.lang.annotation.*;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
@Inherited
public @interface DataTranslate {

    /**
     * translator
     * @return DataTranslator class
     */
    Class<? extends DataTranslator> translator();

    /**
     *
     * @return
     */
    String type();

    /**
     * target fill field
     * @return
     */
    String target();

}
