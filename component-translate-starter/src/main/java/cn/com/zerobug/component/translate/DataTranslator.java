package cn.com.zerobug.component.translate;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
public interface DataTranslator {

    /**
     * 数据翻译
     * @param metadata 数据元数据
     * @param target 数据目标
     * @param <T> 数据目标类型
     */
    <T> void translation(TranslateObjectMetadata metadata, T target);

}
