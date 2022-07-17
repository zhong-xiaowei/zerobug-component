package cn.com.zerobug.component.cache;

import java.time.Duration;
import java.util.Map;

/**
 * 缓存顶级接口
 * 后期如果做多级缓存，可以继承该接口
 *
 * @author zhongxiaowei
 * @date 2022/3/16
 */
public interface CacheCommand {

    /**
     * 获取缓存实例组件
     *
     * @return 具体实现
     */
    <S> S getInstance();

    /**
     * 获取 string value
     *
     * @param key key
     * @param <T> value
     * @return value
     */
    <T> T getValue(String key);

    /**
     * 获取 String value
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param <T>            value类型
     * @return value
     */
    <T> T getValue(CacheKeyDefine cacheKeyDefine);

    /**
     * 获取hash内的value
     *
     * @param cacheKeyDefine 缓存key定义
     * @param hkey           hashkey
     * @param <T>            value
     * @return value
     */
    <T> T getHashValue(CacheKeyDefine cacheKeyDefine, Object hkey);

    /**
     * 获取hash Map结构
     *
     * @param cacheKeyDefine 缓存key定义
     * @return Map
     */
    <K, V> Map<K, V> getHashMap(CacheKeyDefine cacheKeyDefine);

    /**
     * 设置 string 类型的 value
     *
     * @param key     对应的 key
     * @param value   对应的 值
     * @param timeout 超时时间
     */
    <V> void setValue(String key, V value, Duration timeout);

    /**
     * 设置 string 类型的 value
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param value          对应的 值
     */
    <V> void setValue(CacheKeyDefine cacheKeyDefine, V value);

    /**
     * 设置 Map 结构
     *
     * @param cacheKeyDefine 缓存定义
     * @param map            Map对象
     */
    <K, V> void setHashMap(CacheKeyDefine cacheKeyDefine, Map<K, V> map);

    /**
     * 设置 hash 的value
     *
     * @param cacheKeyDefine 缓存定义
     * @param hkey           hash key定义
     * @param hval           hash value值
     */
    <K, V> void setHashValue(CacheKeyDefine cacheKeyDefine, K hkey, V hval);

    /**
     * 获取过期时间
     *
     * @param key key
     * @return 过期时间
     */
    Long getExpire(String key);

    /**
     * 获取过期时间
     *
     * @param cacheKeyDefine
     * @return 过期时间
     */
    Long getExpire(CacheKeyDefine cacheKeyDefine);

    /**
     * 设置过期时间
     *
     * @param key     对应的 key
     * @param timeout 超时时间
     */
    void setExpire(String key, Duration timeout);

    /**
     * 删除 key
     *
     * @param key key
     */
    void removeKey(String key);

    /**
     * 删除 key
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     */
    void removeKey(CacheKeyDefine cacheKeyDefine);

    /**
     * 链表 左弹出元素
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param <T>
     * @return 左弹出的元素
     */
    <T> T listLeftPop(CacheKeyDefine cacheKeyDefine);

    /**
     * 链表 右弹出元素
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param <T>
     * @return 右弹出的元素
     */
    <T> T listRightPop(CacheKeyDefine cacheKeyDefine);

    /**
     * 链表 左插入元素
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param value          元素值
     */
    <V> void listLeftPush(CacheKeyDefine cacheKeyDefine, V value);

    /**
     * 链表 右插入元素
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param value          元素值
     */
    <V> void listRightPush(CacheKeyDefine cacheKeyDefine, V value);

    /**
     * 自增
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param by             自增量
     * @return 自增后的值
     */
    long increment(CacheKeyDefine cacheKeyDefine, int by);

    /**
     * 自减
     *
     * @param cacheKeyDefine {@link CacheKeyDefine}
     * @param by             自减量
     * @return 自减后的值
     */
    long decrement(CacheKeyDefine cacheKeyDefine, int by);

}
