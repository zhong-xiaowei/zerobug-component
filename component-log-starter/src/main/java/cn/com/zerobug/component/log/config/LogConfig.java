package cn.com.zerobug.component.log.config;

import cn.com.zerobug.component.log.properties.LogProperties;
import cn.com.zerobug.component.log.aspect.ApiLogAspect;
import cn.com.zerobug.component.log.listener.ApiLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zhongxiaowei
 * @date 2022/4/6
 */
@EnableAsync
@Configuration
@EnableConfigurationProperties(LogProperties.class)
@Slf4j
public class LogConfig {

    @Bean
    public ApiLogAspect apiLogAspect() {
        return new ApiLogAspect();
    }

    @Bean
    public ApiLogListener apiLogListener() {
        return new ApiLogListener(operateDTO -> log.info("记录操作日志：{}", operateDTO));
    }

}
