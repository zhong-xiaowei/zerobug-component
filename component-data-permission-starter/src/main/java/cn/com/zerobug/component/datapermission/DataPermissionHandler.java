package cn.com.zerobug.component.datapermission;

import cn.com.zerobug.component.datapermission.core.DataPermissionsContext;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * @author zhongxiaowei
 * @date 2022/3/1
 */
public interface DataPermissionHandler {

    /**
     * 匹配规则，为true 这个处理器才会运行
     * @param context
     * @return
     */
    boolean matching(DataPermissionsContext context);

    /**
     * 处理 select 语句的权限过滤
     * @param plainSelect
     * @param index
     * @param context
     */
    void handleSelectExpressions(PlainSelect plainSelect, int index, DataPermissionsContext context);

}
