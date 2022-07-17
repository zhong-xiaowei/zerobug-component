package cn.com.zerobug.component.log.listener;

import cn.com.zerobug.component.log.model.LogOperateDTO;
import cn.com.zerobug.component.log.event.ApiLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * @author zhongxiaowei
 * @date 2022/4/6
 */
public class ApiLogListener {

    private final Consumer<LogOperateDTO> consumer;

    public ApiLogListener(Consumer<LogOperateDTO> consumer) {
        this.consumer = consumer;
    }

    @Async
    @EventListener
    public void saveApiLog(ApiLogEvent event) {
        LogOperateDTO logOperateDTO = (LogOperateDTO) event.getSource();
        consumer.accept(logOperateDTO);
    }

}
