package cn.com.zerobug.component.filter.exception;

import cn.com.zerobug.component.filter.enums.IErrorCode;

/**
 * @author zhongxiaowei
 * @date 2021/12/27
 */
public class IllegalRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private IErrorCode errorCode;

    public IllegalRequestException() {
    }

    public IllegalRequestException(String message) {
        super(message);
    }

    public IllegalRequestException(String message, IErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public IllegalRequestException(IErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public IllegalRequestException(Throwable cause) {
        super(cause);
    }

    public IllegalRequestException(IErrorCode errorCode, Throwable cause) {
        super(errorCode.getMsg(), cause);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
