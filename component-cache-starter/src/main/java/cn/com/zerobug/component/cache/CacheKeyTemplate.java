package cn.com.zerobug.component.cache;

import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * @author zhongxiaowei
 * @date 2022/3/16
 */
@Getter
public class CacheKeyTemplate {

    /**
     * 缓存模板
     */
    @NonNull
    private final String   template;
    /**
     * 超时时间
     */
    private       Duration timeout;

    public CacheKeyTemplate(@NonNull String template) {
        this.template = template;
    }

    public CacheKeyTemplate(@NonNull String template, Duration timeout) {
        this.template = template;
        this.timeout = timeout;
    }

    public static CacheKeyTemplate build(String template) {
        return new CacheKeyTemplate(template);
    }

    public static CacheKeyTemplate build(String template, Duration timeout) {
        return new CacheKeyTemplate(template, timeout);
    }

    public CacheKeyDefine getKeyDefine(String... suffix) {
        return CacheKeyDefine.of(this, suffix);
    }
}
