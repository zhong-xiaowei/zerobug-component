package cn.com.zerobug.component.cache.lock;

import cn.com.zerobug.common.utils.TimerUtil;
import cn.hutool.core.collection.ListUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static lombok.Lombok.checkNotNull;

/**
 * TODO 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 * 未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善未完善
 *
 * 问题一：未测试可用性
 * 问题二：集群模式存在锁错乱
 *
 * @author zhongxiaowei
 * @contact zhongxiaowei.nice@gmail.com
 * @date 2022/6/2
 */
public class RedisTemplateDistributedLock implements DistributedLock {

    private static final int               NO_WAIT_TIME            = -1;
    private static final int               DEFAULT_EXPIRATION_TIME = 300000;
    private static final RedisScript<Long> LOCK_LUA_SCRIPT         = new DefaultRedisScript<>("if redis.call('EXISTS', KEYS[1]) " +
            "== 0 then redis.call('set', KEYS[1], ARGV[1]) redis.call('expire', KEYS[1], ARGV[2]) " +
            "return redis.call('get', KEYS[1]) else return 0 end");
    private static final RedisScript<Long> LOCK_RENEWAL_LUA_SCRIPT = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) " +
            "== ARGV[1] then redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 2 end");


    private final StringRedisTemplate      redisTemplate;
    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 2,
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, "watchdog-thread-" + r.hashCode());
                        }
                    });

    public RedisTemplateDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquire(String key) {
        return acquire(key, NO_WAIT_TIME, null);
    }

    @Override
    public boolean acquire(String key, long acquireTimeout, TimeUnit timeUnit) {
        return acquire(key, acquireTimeout, timeUnit, -1);
    }

    @Override
    public boolean acquire(String key, long acquireTimeout, TimeUnit timeUnit, long LeaseMilliseconds) {
        return tryAcquire(key, acquireTimeout, timeUnit, LeaseMilliseconds);
    }

    @Override
    public boolean release(String key) {
        return false;
    }

    private boolean tryAcquire(String key, long acquireTimeout, TimeUnit timeUnit, long LeaseMilliseconds) {
        // 是否有等待超时
        boolean waitTimeout          = acquireTimeout > NO_WAIT_TIME;
        long    acquireTimeoutMillis = 0;
        if (waitTimeout) {
            checkNotNull(timeUnit, "时间单位不能为空");
            acquireTimeoutMillis = timeUnit.toMillis(acquireTimeout);
        }
        final long id           = Thread.currentThread().getId();
        boolean    obtainedLock = false;
        while (true) {
            if (waitTimeout && System.currentTimeMillis() > acquireTimeoutMillis) {
                return false;
            }
            boolean haveLease = LeaseMilliseconds != -1;
            long result = redisTemplate.execute(LOCK_LUA_SCRIPT, ListUtil.list(false, key), id,
                    haveLease ? Expiration.milliseconds(LeaseMilliseconds) :
                            Expiration.milliseconds(DEFAULT_EXPIRATION_TIME));
            obtainedLock = id == result;
            if (obtainedLock && !haveLease) {
                // 获取锁成功，并且没有租约时间，开启看门狗
                renewal(key, 10);
                return true;
            }
        }
    }

    private void renewal(String key, Integer maxCount) {
        final long id = Thread.currentThread().getId();
        TimerUtil.newTimeout(task -> {
            if (maxCount == 0) {
                return;
            }
            long renewResult = redisTemplate.execute(LOCK_RENEWAL_LUA_SCRIPT, ListUtil.list(false, key),
                    id, Expiration.milliseconds(DEFAULT_EXPIRATION_TIME));
            if (renewResult == id) {
                renewal(key, maxCount - 1);
            } else {
                return;
            }
        }, DEFAULT_EXPIRATION_TIME / 3, TimeUnit.MILLISECONDS);
    }

}
