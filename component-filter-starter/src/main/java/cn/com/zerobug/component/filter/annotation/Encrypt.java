package cn.com.zerobug.component.filter.annotation;

import java.lang.annotation.*;

/**
 * @author zhongxiaowei
 * @date 2022/1/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Encrypt {

    /**
     * 是否开启 默认是开启
     *
     * @return
     */
    boolean open() default true;

    /**
     * 加解密策略
     * REQUEST_AND_RESPONSE  -> 请求解密 返回加密
     * REQUEST_DECRYPT       -> 请求解密，返回不做操作
     * RESPONSE_ENCRYPT      -> 请求不做操作， 返回加密
     *
     * @return
     */
    EncryptStrategy strategy() default EncryptStrategy.REQUEST_AND_RESPONSE;

}
