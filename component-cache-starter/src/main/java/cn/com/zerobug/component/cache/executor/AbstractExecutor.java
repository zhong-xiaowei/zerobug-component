package cn.com.zerobug.component.cache.executor;

import cn.com.zerobug.component.cache.CacheCommand;
import cn.com.zerobug.component.cache.CacheKeyDefine;

import java.time.Duration;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/3/16
 */
public abstract class AbstractExecutor implements CacheCommand {

    @Override
    public <T> T getValue(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getValue(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getHashValue(CacheKeyDefine cacheKeyDefine, Object hkey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K,V> Map<K,V> getHashMap(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> void setValue(String key, V value, Duration timeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> void setValue(CacheKeyDefine cacheKeyDefine, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> void setHashMap(CacheKeyDefine cacheKeyDefine, Map<K, V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K,V> void setHashValue(CacheKeyDefine cacheKeyDefine, K hkey, V hval) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getExpire(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getExpire(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExpire(String key, Duration timeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeKey(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeKey(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T listLeftPop(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T listRightPop(CacheKeyDefine cacheKeyDefine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> void listLeftPush(CacheKeyDefine cacheKeyDefine, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> void listRightPush(CacheKeyDefine cacheKeyDefine, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long increment(CacheKeyDefine cacheKeyDefine, int by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long decrement(CacheKeyDefine cacheKeyDefine, int by) {
        throw new UnsupportedOperationException();
    }

    protected boolean withTimeout(Duration duration) {
        if (duration == null || duration.isZero()) {
            return false;
        }
        return true;
    }
}
