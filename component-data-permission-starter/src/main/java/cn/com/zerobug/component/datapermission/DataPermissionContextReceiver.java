package cn.com.zerobug.component.datapermission;

import cn.com.zerobug.component.datapermission.core.DataPermissionsContext;

/**
 * @author zhongxiaowei
 * @date 2022/3/3
 */
@FunctionalInterface
public interface DataPermissionContextReceiver {

    /**
     * context 接收
     * @return
     */
    DataPermissionsContext receive();

}
