package cn.com.zerobug.component.filter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongxiaowei
 * @date 2021/12/29
 */
@Configuration
@Import(value = {
        ZerobugFilterChainAutoConfiguration.class
})
public class ZerobugAutoConfiguration {

}
