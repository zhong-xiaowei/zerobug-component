package cn.com.zerobug.component.swagger.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import springfox.documentation.service.ParameterType;

import java.io.Serializable;
import java.util.*;

/**
 * @author zhongxiaowei
 * @date 2022/4/5
 */
@Data
@ConfigurationProperties(prefix = Swagger3Properties.PREFIX)
public class Swagger3Properties implements Serializable {

    public static final String PREFIX = "zerobug.swagger";
    private static final long serialVersionUID = 1895741957667699827L;

    /**
     * 开启swagger的环境 多个以，分割
     */
    private String enableEnv = "dev,test";
    /**
     * 全局参数 优先级最低
     */
    private GlobalConfig globalConfig;
    /**
     * 接口文档分组  key 为分组名称，value为分组详细配置
     */
    private Map<String, GroupConfig> groups = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalConfig {

        /**
         * 版本号
         */
        private String version = "3.0.0";
        /**
         * 标题
         */
        private String title = "Api 接口文档";
        /**
         * 描述
         */
        private String description = "Api 接口文档";
        /**
         * 服务条款网址
         */
        private String termsOfServiceUrl = "";
        /**
         * 协议证书
         */
        private String license = "MIT License";
        /**
         * 协议证书网址
         */
        private String licenseUrl = "https://opensource.org/licenses/MIT";
        /**
         * 联系人
         */
        private Contact contact = new Contact("", "", "");
        /**
         * 包路径
         */
        @NonNull
        private String packagePath;
        /**
         * 接口包含路径
         */
        private List<String> includePaths = Arrays.asList("/**");
        /**
         * 接口排除路径
         */
        private List<String> excludePaths = new ArrayList<>();
        /**
         * 请求参数
         */
        private List<ApiRequestParameter> apiRequestParameters;
    }

    @Data
    @NoArgsConstructor
    public static class GroupConfig {
        /**
         * 版本号
         */
        private String version;
        /**
         * 标题
         */
        private String title;
        /**
         * 描述
         */
        private String description;
        /**
         * 服务条款网址
         */
        private String termsOfServiceUrl;
        /**
         * 协议证书
         */
        private String license;
        /**
         * 协议证书网址
         */
        private String licenseUrl;
        /**
         * 分组接口包路径
         */
        @NonNull
        private String packagePath;
        /**
         * 接口联系人
         */
        private Contact contact;
        /**
         * 分组接口包含路径
         */
        private List<String> includePaths;
        /**
         * 分组接口排除路径
         */
        private List<String> excludePaths;
        /**
         * 分组接口请求参数
         */
        private List<ApiRequestParameter> apiRequestParameters;
    }

    @Data
    @NoArgsConstructor
    public static class ApiRequestParameter {
        /**
         * 参数名
         */
        private String name;
        /**
         * 参数类型
         */
        private ParameterType in;
        /**
         * 参数描述
         */
        private String description;
        /**
         * 是否必须
         */
        private Boolean required;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        /**
         * 接口联系 人
         */
        private String contactName;
        /**
         * 接口联系方式 主页
         */
        private String contactUrl;
        /**
         * 接口联系方式 邮箱
         */
        private String contactEmail;
    }

}
