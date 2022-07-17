package cn.com.zerobug.component.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhongxiaowei
 * @date 2022/4/6
 */
public class ApiLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1902563826319142502L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ApiLogEvent(Object source) {
        super(source);
    }

}
