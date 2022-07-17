package cn.com.zerobug.component.filter.enums;

/**
 * @author zhongxiaowei
 */
public enum ErrorCode implements IErrorCode{

    //请求成功
    SUCCESS(0, "SUCCESS"),
    //业务异常
    FAILURE(-1, "FAILURE"),
    ERROR_SYSTEM(10, "系统异常，未知异常[%s]"),
    ERROR_PARAMS(11, "请求参数有误[%s]"),
    ERROR_DB(12, "数据服务异常"),
    ERROR_UN_AUTH(13, "未授权"),
    ERROR_FAIL_AUTH(14, "身份认证失败"),
    ERROR_LOGIN_EXPIRED(15, "登录已失效");

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
