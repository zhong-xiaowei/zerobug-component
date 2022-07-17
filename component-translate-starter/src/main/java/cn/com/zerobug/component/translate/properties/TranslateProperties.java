package cn.com.zerobug.component.translate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
@ConfigurationProperties(prefix = TranslateProperties.PREFIX)
public class TranslateProperties {

    public static final String PREFIX = "zerobug.translate";

    private Boolean enable = true;
    private String packagePath;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}
