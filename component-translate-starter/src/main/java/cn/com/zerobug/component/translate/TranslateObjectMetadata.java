package cn.com.zerobug.component.translate;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
@Data
@ToString
public class TranslateObjectMetadata {

    /**
     * 翻译对象类
     */
    private Class sourceClass;
    /**
     * 翻译数据收集器
     */
    private DataTranslator translator;
    /**
     * 翻译类型
     */
    private String translateType;
    /**
     * 翻译字段
     */
    private String fieldName;
    /**
     * 翻译显示字段
     */
    private String targetName;

}
