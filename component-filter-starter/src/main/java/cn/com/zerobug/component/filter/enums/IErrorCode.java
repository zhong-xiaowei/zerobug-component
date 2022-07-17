package cn.com.zerobug.component.filter.enums;

/**
 * @author zhongxiaowei
 * @date 2021/12/27
 */
public interface IErrorCode {

    /**
     * 获取返回码
     * @return
     */
    int getCode();

    /**
     * 获取返回消息
     * @return
     */
    String getMsg();

}
