package cn.com.zerobug.component.translate;

import cn.hutool.extra.spring.SpringUtil;

/**
 * @author zhongxiaowei
 * @date 2022/4/16
 */
public class TranslateHelper {

    private static DataTranslateManager dataTranslateManager;

    public static DataTranslateManager getInstance() {
        if (dataTranslateManager == null) {
            synchronized (TranslateHelper.class) {
                if (dataTranslateManager == null) {
                    dataTranslateManager = SpringUtil.getBean(DataTranslateManager.class);
                }
            }
        }
        return dataTranslateManager;
    }

    public static <T> void translate(T t) {
        getInstance().transform(t);
    }


}
