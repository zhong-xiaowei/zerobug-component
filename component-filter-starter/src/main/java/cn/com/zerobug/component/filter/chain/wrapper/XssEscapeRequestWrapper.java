package cn.com.zerobug.component.filter.chain.wrapper;

import cn.com.zerobug.component.filter.chain.SecurityHelper;
import cn.hutool.core.util.StrUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
public class XssEscapeRequestWrapper extends RepeatedlyRequestWrapper {

    public XssEscapeRequestWrapper(HttpServletRequest request) throws IOException, ServletException {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String body = super.getBody();
        if (StrUtil.isEmpty(body)) {
            return super.getInputStream();
        }
        body = SecurityHelper.xssClean(body);
        super.bodyArray = body.getBytes();
        return super.getInputStream();
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isEmpty(value)) {
            return null;
        }
        return SecurityHelper.xssClean(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        Map<String, String[]> resultMap    = new LinkedHashMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = SecurityHelper.xssClean(values[i]);
            }
            resultMap.put(entry.getKey(), values);
        }
        return resultMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        for (int i = 0; i < parameterValues.length; i++) {
            parameterValues[i] = SecurityHelper.xssClean(parameterValues[i]);
        }
        return parameterValues;
    }

    static class RequestCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public RequestCachingInputStream(byte[] bytes) {
            inputStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }

}
