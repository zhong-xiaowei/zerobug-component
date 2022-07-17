package cn.com.zerobug.component.cache.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author zhongxiaowei
 * @contact zhongxiaowei.nice@gmail.com
 * @date 2022/6/2
 */
public interface DistributedLock {

    /**
     * 加锁
     *
     * @param key 锁的key
     * @return true:加锁成功，false:加锁失败
     */
    boolean acquire(String key);

    /**
     * 加锁
     * @param key 锁的key
     * @param acquireTimeout 获取锁的超时时间
     * @param timeUnit 超时时间单位
     * @return true:加锁成功，false:加锁失败
     */
    boolean acquire(String key, long acquireTimeout, TimeUnit timeUnit);

    /**
     * 加锁
     *
     * @param key            锁的key
     * @param acquireTimeout 等待超时时间
     * @param timeUnit       时间单位
     * @return true:加锁成功，false:加锁失败
     */
    boolean acquire(String key, long acquireTimeout, TimeUnit timeUnit, long LeaseMilliseconds);

    /**
     * 解锁
     *
     * @param key 锁的key
     * @return true:解锁成功，false:解锁失败
     */
    boolean release(String key);

}
