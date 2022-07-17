package cn.com.zerobug.component.orm.interceptor;

import cn.hutool.core.lang.Assert;
import cn.com.zerobug.component.orm.properties.DbReadWriteProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/11/2 1:40 下午
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class AutoReadWriteSeparateInterceptor implements Interceptor {

    private AtomicInteger nextDataSourceCyclicCounter;
    private String        writeDataSourceName;
    private List<String>  readDataSourceNames = new ArrayList<>(4);

    public AutoReadWriteSeparateInterceptor(DbReadWriteProperties dbReadWriteProperties) {
        this.nextDataSourceCyclicCounter = new AtomicInteger(0);
        Assert.notBlank(dbReadWriteProperties.getWriteDb(), "write-db cannot empty");
        this.writeDataSourceName = dbReadWriteProperties.getWriteDb();
        Assert.notBlank(dbReadWriteProperties.getReadDb(), "read-db cannot empty");
        readDataSourceNames.addAll(Arrays.asList(dbReadWriteProperties.getReadDb().split(",")));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[]        args = invocation.getArgs();
        MappedStatement ms   = (MappedStatement) args[0];
        try {
            String executeDataSource = null;
            if (SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
                int count       = readDataSourceNames.size();
                int nextDbIndex = 0;
                if (count > 1) {
                    // 负载均衡
                    nextDbIndex = loadBalancing(count);
                }
                executeDataSource = readDataSourceNames.get(nextDbIndex);
            } else {
                executeDataSource = writeDataSourceName;
            }
            DynamicDataSourceContextHolder.push(executeDataSource);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

    private int loadBalancing(int count) {
        for (; ; ) {
            int i    = nextDataSourceCyclicCounter.get();
            int next = (i + 1) % count;
            if (nextDataSourceCyclicCounter.compareAndSet(i, next)) {
                return next;
            }
        }
    }
}
