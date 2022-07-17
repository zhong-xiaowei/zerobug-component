package cn.com.zerobug.component.log.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhongxiaowei
 * @date 2022/4/2
 */
@Data
public class LogOperateDTO implements Serializable {

    private static final long serialVersionUID = 8923278282566570898L;

    /**
     * 追踪ID
     */
    private String  traceId;
    /**
     * 请求方法
     */
    private String  requestMethod;
    /**
     * 请求路径
     */
    private String  requestUrl;
    /**
     * 用户Id
     */
    private Long    userId;
    /**
     * 请求者 ip
     */
    private String  requestIp;
    /**
     * 请求使用的 浏览器 UA
     */
    private String  requsetUserAgent;
    /**
     * 方法作用描述
     */
    private String  description;
    /**
     * java类型路径
     */
    private String  javaClassPath;
    /**
     * Java 方法名
     */
    private String  javaMethod;
    /**
     * Java 方法的参数
     */
    private String  javaMethodArgs;
    /**
     * 操作时间
     */
    private Date    startTime;
    /**
     * 执行时长
     */
    private Integer duration;
    /**
     * 结果码
     */
    private Integer resultCode;
    /**
     * 结果提示
     */
    private String  resultMsg;
    /**
     * 结果数据
     */
    private String  resultData;

    public static LogOperateDTOBuilder builder() {
        return LogOperateDTOBuilder.aLogOperateDTO();
    }

    @SuppressWarnings("AlibabaClassNamingShouldBeCamel")
    public static final class LogOperateDTOBuilder {
        private Long    userId;
        private String  traceId;
        private String  requestMethod;
        private String  requestUrl;
        private String  requestIp;
        private String  requsetUserAgent;
        private String  description;
        private String  javaClassPath;
        private String  javaMethod;
        private String  javaMethodArgs;
        private Date    startTime;
        private Integer duration;
        private Integer resultCode;
        private String  resultMsg;
        private String  resultData;

        private LogOperateDTOBuilder() {
        }

        public static LogOperateDTOBuilder aLogOperateDTO() {
            return new LogOperateDTOBuilder();
        }

        public LogOperateDTOBuilder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public LogOperateDTOBuilder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public LogOperateDTOBuilder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public LogOperateDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LogOperateDTOBuilder requestIp(String requestIp) {
            this.requestIp = requestIp;
            return this;
        }

        public LogOperateDTOBuilder requsetUserAgent(String requsetUserAgent) {
            this.requsetUserAgent = requsetUserAgent;
            return this;
        }

        public LogOperateDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LogOperateDTOBuilder javaClassPath(String javaClassPath) {
            this.javaClassPath = javaClassPath;
            return this;
        }

        public LogOperateDTOBuilder javaMethod(String javaMethod) {
            this.javaMethod = javaMethod;
            return this;
        }

        public LogOperateDTOBuilder javaMethodArgs(String javaMethodArgs) {
            this.javaMethodArgs = javaMethodArgs;
            return this;
        }

        public LogOperateDTOBuilder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public LogOperateDTOBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public LogOperateDTOBuilder resultCode(Integer resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public LogOperateDTOBuilder resultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
            return this;
        }

        public LogOperateDTOBuilder resultData(String resultData) {
            this.resultData = resultData;
            return this;
        }

        public LogOperateDTO build() {
            LogOperateDTO logOperateDTO = new LogOperateDTO();
            logOperateDTO.setTraceId(traceId);
            logOperateDTO.setRequestMethod(requestMethod);
            logOperateDTO.setRequestUrl(requestUrl);
            logOperateDTO.setUserId(userId);
            logOperateDTO.setRequestIp(requestIp);
            logOperateDTO.setRequsetUserAgent(requsetUserAgent);
            logOperateDTO.setDescription(description);
            logOperateDTO.setJavaClassPath(javaClassPath);
            logOperateDTO.setJavaMethod(javaMethod);
            logOperateDTO.setJavaMethodArgs(javaMethodArgs);
            logOperateDTO.setStartTime(startTime);
            logOperateDTO.setDuration(duration);
            logOperateDTO.setResultCode(resultCode);
            logOperateDTO.setResultMsg(resultMsg);
            logOperateDTO.setResultData(resultData);
            return logOperateDTO;
        }
    }
}
