package cn.com.zerobug.component.cache.executor;

import cn.com.zerobug.component.cache.CacheKeyDefine;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;

/**
 * @author zhongxiaowei
 * @date 2022/3/16
 */
public class RedisTemplateExecutor extends AbstractExecutor {

    private final RedisTemplate redisTemplate;

    public RedisTemplateExecutor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplate getInstance() {
        return redisTemplate;
    }

    @Override
    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T getValue(CacheKeyDefine cacheKeyDefine) {
        return getValue(cacheKeyDefine.getKey());
    }

    @Override
    public <T> T getHashValue(CacheKeyDefine cacheKeyDefine, Object hkey) {
        return (T) redisTemplate.opsForHash().get(cacheKeyDefine.getKey(), hkey);
    }

    @Override
    public <K, V> Map<K, V> getHashMap(CacheKeyDefine cacheKeyDefine) {
        return redisTemplate.opsForHash().entries(cacheKeyDefine.getKey());
    }

    @Override
    public <V> void setValue(String key, V value, Duration timeout) {
        if (!withTimeout(timeout)) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout);
        }
    }

    @Override
    public <V> void setValue(CacheKeyDefine cacheKeyDefine, V value) {
        this.setValue(cacheKeyDefine.getKey(), value, cacheKeyDefine.getCacheKeyTemplate().getTimeout());
    }

    @Override
    public <K, V> void setHashMap(CacheKeyDefine cacheKeyDefine, Map<K, V> map) {
        redisTemplate.opsForHash().putAll(cacheKeyDefine.getKey(), map);
        if (withTimeout(cacheKeyDefine.getCacheKeyTemplate().getTimeout())) {
            redisTemplate.expire(cacheKeyDefine.getKey(), cacheKeyDefine.getCacheKeyTemplate().getTimeout());
        }
    }

    @Override
    public <K,V> void setHashValue(CacheKeyDefine cacheKeyDefine, K hkey, V hval) {
        redisTemplate.opsForHash().put(cacheKeyDefine.getKey(), hkey, hkey);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Long getExpire(CacheKeyDefine cacheKeyDefine) {
        return this.getExpire(cacheKeyDefine.getKey());
    }

    @Override
    public void setExpire(String key, Duration timeout) {
        redisTemplate.expire(key, timeout);
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeKey(CacheKeyDefine cacheKeyDefine) {
        this.removeKey(cacheKeyDefine.getKey());
    }

    @Override
    public <T> T listLeftPop(CacheKeyDefine cacheKeyDefine) {
        return (T) redisTemplate.opsForList().leftPop(cacheKeyDefine.getKey());
    }

    @Override
    public <T> T listRightPop(CacheKeyDefine cacheKeyDefine) {
        return (T) redisTemplate.opsForList().rightPop(cacheKeyDefine.getKey());
    }

    @Override
    public <V> void listLeftPush(CacheKeyDefine cacheKeyDefine, V value) {
        redisTemplate.opsForList().leftPush(cacheKeyDefine.getKey(), value);
    }

    @Override
    public <V> void listRightPush(CacheKeyDefine cacheKeyDefine, V value) {
        redisTemplate.opsForList().rightPush(cacheKeyDefine.getKey(), value);
    }

    @Override
    public long increment(CacheKeyDefine cacheKeyDefine, int by) {
        return redisTemplate.opsForValue().increment(cacheKeyDefine.getKey(), by);
    }

    @Override
    public long decrement(CacheKeyDefine cacheKeyDefine, int by) {
        return redisTemplate.opsForValue().decrement(cacheKeyDefine.getKey(), by);
    }
}
