package cn.com.zerobug.component.cache.executor;

import cn.com.zerobug.component.cache.CacheCommand;

/**
 * TODO 多级缓存执行器
 *
 * @author zhongxiaowei
 * @date 2022/3/16
 */
public final class MultistageCacheExecutor extends AbstractExecutor {

    /**
     * 一级缓存
     */
    private final CacheCommand l1;
    /**
     * 二级缓存
     */
    private final CacheCommand l2;
    /**
     * 三级缓存
     */
    private final CacheCommand l3;

    public MultistageCacheExecutor(CacheCommand l1, CacheCommand l2, CacheCommand l3) {
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    @Override
    public MultistageCacheExecutor getInstance() {
        return this;
    }

    public CacheCommand getL1() {
        return l1;
    }

    public CacheCommand getL2() {
        return l2;
    }

    public CacheCommand getL3() {
        return l3;
    }
}
