package cn.com.zerobug.component.datapermission.core;

import cn.com.zerobug.component.datapermission.DataPermissionHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 上下文数据 context
 *
 * @author zhongxiaowei
 * @date 2022/3/3
 */
@Data
public class DataPermissionsContext {

    private Long id;
    private String username;
    private Long deptId;
    private List<String> roleIds;
    private List<String> permissionTypes;
    private AnnotationProperties annotationProperties;

    @Data
    public static class AnnotationProperties {
        private List<AnnotationTableField> tableFields;
        private List<String>                             excludeStatementIds;
        private Class<? extends DataPermissionHandler>[] disabledHandlerClasses;
        private String[]                                 custom;
    }

    @Data
    @Accessors(chain = true)
    public static class AnnotationTableField {
        private String alias;
        private String field;
    }

}
