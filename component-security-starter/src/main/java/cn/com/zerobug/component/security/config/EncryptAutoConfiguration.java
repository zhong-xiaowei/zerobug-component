package cn.com.zerobug.component.security.config;

import cn.com.zerobug.component.security.properties.EncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongxiaowei
 * @contact zhongxiaowei.nice@gmail.com
 * @date 2022/8/11
 */
@Configuration
@EnableConfigurationProperties(value = EncryptProperties.class)
@ComponentScan(value = "cn.com.zerobug.component.security")
public class EncryptAutoConfiguration {



}
