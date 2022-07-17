package cn.com.zerobug.component.filter.chain.matcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhongxiaowei
 * @date 2022/1/1
 */
public interface RequestMatcher {

    /**
     * 请求匹配
     *
     * @param request
     * @return
     * @throws Exception
     */
    boolean matches(HttpServletRequest request) throws Exception;

}
