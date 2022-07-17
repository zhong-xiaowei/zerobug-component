package cn.com.zerobug.component.filter.annotation;

/**
 * 忽略类型
 *      XSS 只忽略XSS
 *      SQL 只忽略SQL
 *      ALL 全部忽略
 * @author zhongxiaowei
 * @date 2021/12/22
 */
public enum IgnoreType {

    /**
     * XSS攻击忽略
     */
    XSS,
    /**
     * SQL攻击忽略
     */
    SQL,
    /**
     * 所有攻击忽略
     */
    ALL,

}
