package cn.com.zerobug.component.filter.chain;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.com.zerobug.component.filter.chain.wrapper.RepeatedlyRequestWrapper;
import cn.com.zerobug.component.filter.exception.IllegalRequestException;
import com.alibaba.fastjson.JSONValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zhongxiaowei
 * @email: <a>zhongxiaowei.nice@gmail.com</a>
 * @date: 2021/7/8
 * @version: 1.0
 */
@Slf4j
public class SecurityHelper {

    private static Pattern SQL_PATTERN = Pattern.compile("(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)", Pattern.CASE_INSENSITIVE);

    /**
     * xss 攻击代码 匹配器
     */
    private static Pattern[] XSS_PATTERNS = new Pattern[]{
            Pattern.compile("(<|%(.*)3C)script(>|%(.*)3E)(.*?)(<|%(.*)3C)/script(>|%(.*)3E)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("(<|%(.*)3C)/script(>|%(.*)3E)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(<|%(.*)3C)script(.*?)(>|%(.*)3E)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("alert(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("<|%(.*)3C", Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile(">|%(.*)3E", Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE),
            Pattern.compile("((alert|on\\w+|function\\s+\\w+)\\s*\\(\\s*(['+\\d\\w](,?\\s*['+\\d\\w]*)*)*\\s*\\))"),
            Pattern.compile("(<|%(.*)3C)(script|iframe|embed|frame|frameset|object|img|applet|body|html|style|layer|link|ilayer|meta|bgsound)"),
            Pattern.compile("(<|%(.*)3C)input(.*?)(>|%(.*)3E)(<|%(.*)3C)/input(>|%(.*)3E)|(<|%(.*)3C)input(.*)/(>|%(.*)3E)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(<|%(.*)3C)span(.*?)(<|%(.*)3C)/span(>|%(.*)3E)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(<|%(.*)3C)div(.*)(<|%(.*)3C)/div(>|%(.*)3E)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("console(.|%2e)log(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("prompt(.*)", Pattern.CASE_INSENSITIVE),
    };

    /**
     * xss转义清洗
     *
     * @param content
     * @return
     */
    public static String xssClean(String content) {
        JSONValidator jsonValidator = JSONValidator.from(content);
        if (jsonValidator.validate()) {
            return SimpleXssEscape.escape(content);
        } else {
            return StringEscapeUtils.escapeHtml4(content);
        }
    }

    /**
     * xss校验
     *
     * @param value 需要校验的字符
     * @return 返回值：true 表示存在xss漏洞，false：不存在
     */
    public static boolean checkPresenceOfXss(String value) {
        boolean isXss = false;
        if (StrUtil.isNotEmpty(value)) {
            for (Pattern scriptPattern : XSS_PATTERNS) {
                Matcher matcher = scriptPattern.matcher(value);
                if (matcher.find()) {
                    isXss = true;
                    break;
                }
            }
        }
        return isXss;
    }

    /**
     * sql校验
     *
     * @param value 检查值
     * @return 存在sql
     */
    public static boolean checkPresenceOfSql(String value) {
        boolean isSql = false;
        if (StrUtil.isNotEmpty(value)) {
            Matcher matcher = SQL_PATTERN.matcher(value);
            if (matcher.find()) {
                isSql = true;
            }
        }
        return isSql;
    }

    /**
     * 尝试找到sql代码
     *
     * @param request
     */
    public static void attemptFindSqlCode(HttpServletRequest request) {
        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String value = entry.getValue();
            if (checkPresenceOfSql(value)) {
                log.error("存在非法SQL代码：{}", value);
                throw new IllegalRequestException("存在非法字符！");
            }
        }
        if (!(request instanceof RepeatedlyRequestWrapper)) {
            return;
        }
        String body = ((RepeatedlyRequestWrapper) request).getBody();
        if (checkPresenceOfSql(body)) {
            log.error("存在非法SQL代码：{}", body);
            throw new IllegalRequestException("存在非法字符！");
        }
        return;
    }

    /**
     * 尝试找到xss攻击代码
     *
     * @param request
     */
    public static void attemptFindXssCode(HttpServletRequest request) {
        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String value = entry.getValue();
            if (checkPresenceOfXss(value)) {
                log.error("存在非法XSS代码：{}", value);
                throw new IllegalRequestException("存在非法字符！");
            }
        }
        if (!(request instanceof RepeatedlyRequestWrapper)) {
            return;
        }
        String body = ((RepeatedlyRequestWrapper) request).getBody();
        if (StrUtil.isNotEmpty(body)) {
            if (checkPresenceOfXss(body)) {
                log.error("存在非法XSS代码：{}", body);
                throw new IllegalRequestException("存在非法字符！");
            }
        }
    }

    static class SimpleXssEscape {

        private static final char[][] TEXT = new char[64][];

        static {
            for (int i = 0; i < TEXT.length; i++) {
                TEXT[i] = new char[]{(char) i};
            }
            TEXT['\''] = "&#039;".toCharArray();
            TEXT['&'] = "&amp;".toCharArray();
            TEXT['<'] = "&lt;".toCharArray();
            TEXT['>'] = "&gt;".toCharArray();
        }

        public static String escape(String content) {
            int l;
            if ((content == null) || ((l = content.length()) == 0)) {
                return StrUtil.EMPTY;
            }
            StringBuilder buffer = new StringBuilder(l + (l >> 2));
            char          c;
            for (int i = 0; i < l; i++) {
                c = content.charAt(i);
                if (c < 64) {
                    buffer.append(TEXT[c]);
                } else {
                    buffer.append(c);
                }
            }
            return buffer.toString();
        }

    }

}
