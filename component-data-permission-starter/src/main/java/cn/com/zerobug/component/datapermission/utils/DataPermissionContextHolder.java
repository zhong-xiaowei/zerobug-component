package cn.com.zerobug.component.datapermission.utils;

import cn.com.zerobug.component.datapermission.annotation.DataPermission;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhongxiaowei
 * @date 2022/2/9
 */
public class DataPermissionContextHolder {

    private static final ThreadLocal<LinkedList<DataPermission>> DATA_PERMISSIONS =
            TransmittableThreadLocal.withInitial(LinkedList::new);

    public static DataPermission get() {
        return DATA_PERMISSIONS.get().peekLast();
    }

    public static void add(DataPermission dataPermission) {
        DATA_PERMISSIONS.get().addLast(dataPermission);
    }

    public static DataPermission remove() {
        DataPermission dataPermission = DATA_PERMISSIONS.get().removeLast();
        if (DATA_PERMISSIONS.get().isEmpty()) {
            DATA_PERMISSIONS.remove();
        }
        return dataPermission;
    }

    public static List<DataPermission> getAll() {
        return DATA_PERMISSIONS.get();
    }

    public static void clear() {
        DATA_PERMISSIONS.remove();
    }

}