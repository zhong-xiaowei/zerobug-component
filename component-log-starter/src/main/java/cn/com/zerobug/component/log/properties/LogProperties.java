package cn.com.zerobug.component.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/11/9 2:53 下午
 */
@Data
@ConfigurationProperties(prefix = LogProperties.LOG_PREFIX)
public class LogProperties {

    public static final String LOG_PREFIX = "zerobug.log";

    /**
     * 是否启用日志
     */
    private boolean enable;
    /**
     * 日志推送模式 rabbitmq \ jdbc
     */
    private String  pushMode;
    /**
     * 日志存储方式 es \ jdbc
     */
    private String  storage;

}
