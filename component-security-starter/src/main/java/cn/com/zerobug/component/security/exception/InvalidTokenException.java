package cn.com.zerobug.component.security.exception;

import cn.com.zerobug.common.exception.GeneralException;

/**
 * @author zhongxiaowei
 * @date 2022/3/19
 */
public class InvalidTokenException extends GeneralException {

    private static final long serialVersionUID = -1418150128760315568L;

    public InvalidTokenException(String msg) {
        super(msg);
    }
}
