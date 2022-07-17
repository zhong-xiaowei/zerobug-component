package cn.com.zerobug.component.filter.chain;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
public class CommonRequestRejectHandler implements RequestRejectHandler {

    @Override
    public void afterReject(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        String message = exception.getMessage();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ServletUtil.write(response, message, MediaType.APPLICATION_JSON_VALUE);
    }
}
