package cn.com.zerobug.component.cache;

import cn.com.zerobug.common.context.GlobalContextHolder;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * @author zhongxiaowei
 * @date 2022/3/16
 */
@Getter
public class CacheKeyDefine {

    private final String           key;
    private final CacheKeyTemplate cacheKeyTemplate;

    public CacheKeyDefine(@NonNull CacheKeyTemplate cacheKeyTemplate, String... suffix) {
        this.cacheKeyTemplate = cacheKeyTemplate;
        this.key = buildKey(cacheKeyTemplate, suffix);
    }

    private String buildKey(@NonNull CacheKeyTemplate cacheKeyTemplate, String... suffix) {
        String template = cacheKeyTemplate.getTemplate();
        String cacheKey = StrUtil.format(template, suffix);
        Long   tenant   = GlobalContextHolder.getTenant();
        if (tenant != null) {
            cacheKey = tenant + StrPool.COLON + cacheKey;
        }
        return cacheKey;
    }

    public static CacheKeyDefine of(@NonNull CacheKeyTemplate cacheKeyTemplate, String... suffix) {
        return new CacheKeyDefine(cacheKeyTemplate, suffix);
    }

}
