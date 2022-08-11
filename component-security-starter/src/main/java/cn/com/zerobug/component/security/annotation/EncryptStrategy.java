package cn.com.zerobug.component.security.annotation;

/**
 * @author zhongxiaowei
 * @date 2022/1/24
 */
public enum EncryptStrategy {

    /**
     * 请求和响应加解密
     */
    REQUEST_AND_RESPONSE,
    /**
     * 请求加解密
     */
    REQUEST_DECRYPT,
    /**
     * 响应加解密
     */
    RESPONSE_ENCRYPT

}
