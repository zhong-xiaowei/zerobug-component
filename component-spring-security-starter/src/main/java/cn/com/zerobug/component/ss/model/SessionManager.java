package cn.com.zerobug.component.ss.model;

import cn.hutool.core.util.StrUtil;
import cn.com.zerobug.component.cache.CacheCommand;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * @author zhongxiaowei
 * @date 2022/3/18
 */
public class SessionManager {

    private static final String SESSION_USER_KEY = "session_user:{}";

    private CacheCommand cacheCommand;

    public SessionManager(CacheCommand cacheCommand) {
        this.cacheCommand = cacheCommand;
    }

    /**
     * 保存会话
     *
     * @param authenticatedUser
     * @param secondsSupplier
     */
    public void saveSession(AuthenticatedUser authenticatedUser, Supplier<Integer> secondsSupplier) {
        String key = keyFormat(SESSION_USER_KEY, authenticatedUser.getId());
        cacheCommand.setValue(key, authenticatedUser, Duration.ofSeconds(secondsSupplier.get()));
    }

    /**
     * 删除会话
     * @param id 用户唯一标识
     */
    public void removeSession(Object id) {
        String key = keyFormat(SESSION_USER_KEY, id);
        cacheCommand.removeKey(key);
    }

    /**
     * 获取会话
     * @param id 用户唯一标识
     * @return 会话用户
     */
    public AuthenticatedUser getSession(Object id) {
        String key = keyFormat(SESSION_USER_KEY, id);
        return cacheCommand.getValue(key);
    }

    public static String keyFormat(String template, Object... args) {
        return StrUtil.format(template, args);
    }

}
