package cn.com.zerobug.component.translate;

/**
 * @author zhongxiaowei
 * @date 2022/4/15
 */
public interface DataTranslateManager {

    /**
     * initialize
     */
    void initialize();

    /**
     * get data by type
     * @param t type
     * @param <T> type
     * @return
     */
    <T> void transform(T t);

}
