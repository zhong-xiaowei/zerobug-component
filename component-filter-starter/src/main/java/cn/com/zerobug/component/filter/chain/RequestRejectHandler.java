package cn.com.zerobug.component.filter.chain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
@FunctionalInterface
public interface RequestRejectHandler {

    /**
     * 拒绝处理
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     */
    void afterReject(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException;

}
