package cn.com.zerobug.component.datapermission.core;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * Mybatis Plus 拦截器
 *
 * @author zhongxiaowei
 * @date 2022/3/2
 */
public class MybatisPlusDataPermissionInterceptor implements InnerInterceptor {

    private DataPermissionSqlResolver dataPermissionSqlResolver;

    public MybatisPlusDataPermissionInterceptor(DataPermissionSqlResolver dataPermissionSqlResolver) {
        this.dataPermissionSqlResolver = dataPermissionSqlResolver;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        dataPermissionSqlResolver.preprocessing(boundSql, ms, ms.getSqlCommandType());
    }

}
