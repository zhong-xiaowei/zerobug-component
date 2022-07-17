package cn.com.zerobug.component.filter.properties;

import java.util.Set;

/**
 * @author zhongxiaowei
 * @date 2021/12/31
 */
public abstract class AbstractSecurityProperties extends AbstractProperties {

    /**
     * 是开启的
     * @return
     */
    public abstract boolean getEnable();

    /**
     * 获取包含路径
     * @return
     */
    public abstract String getIncludePaths();

    /**
     * 获取忽略路径集合
     * @return
     */
    public abstract Set<String> getIgnorePaths();

}
