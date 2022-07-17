package cn.com.zerobug.component.datapermission.core;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author zhongxiaowei
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class MybatisDataPermissionInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(MybatisDataPermissionInterceptor.class);

    private DataPermissionSqlResolver dataPermissionSqlResolver;

    public MybatisDataPermissionInterceptor(DataPermissionSqlResolver dataPermissionSqlResolver) {
        this.dataPermissionSqlResolver = dataPermissionSqlResolver;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatements = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatements.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.SELECT) {
            dataPermissionSqlResolver.preprocessing(mappedStatements.getBoundSql(args[1]), mappedStatements, sqlCommandType);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}