package cn.com.zerobug.component.filter.chain.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: zhongxiaowei
 * @date: 2021/7/6
 */
@Slf4j
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {

    protected byte[] bodyArray;

    public RepeatedlyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bodyArray = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bodyArray);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取body
     *
     * @return #body
     */
    public String getBody() {
        return new String(bodyArray);
    }

    /**
     * 设置body
     *
     * @param body
     */
    public void setBody(byte[] body) {
        this.bodyArray = body;
    }

}

