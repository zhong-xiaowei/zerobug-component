package cn.com.zerobug.component.datapermission.core;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 数据权限 sql 解析器
 * @author zhongxiaowei
 * @date 2022/3/1
 */
public interface DataPermissionSqlResolver {

    /**
     * 数据权限预处理
     * @param boundSql sql
     * @param mappedStatements 语句
     * @param sqlCommandType sql类型
     */
    void preprocessing(BoundSql boundSql, MappedStatement mappedStatements, SqlCommandType sqlCommandType);

}
