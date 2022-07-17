package cn.com.zerobug.component.log.config;

import org.slf4j.TtlMdcAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhongxiaowei
 * @date 2022/4/4
 */
public class TtlMdcAdapterInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TtlMdcAdapter.getInstance();
    }
}
