package cn.com.zerobug.component.orm.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/11/2 3:49 下午
 */
@Data
@ConfigurationProperties(prefix = "osc.db-read-write")
public class DbReadWriteProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 写 数据库 名
     */
    private String  writeDb;
    /**
     * 读 数据库名 以 , 分割
     */
    private String  readDb;

}
