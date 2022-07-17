package cn.com.zerobug.component.orm.config;

import cn.com.zerobug.component.orm.interceptor.AutoReadWriteSeparateInterceptor;
import cn.com.zerobug.component.orm.method.BatchInsert;
import cn.com.zerobug.component.orm.properties.DbReadWriteProperties;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhongxiaowei
 * @version V1.0
 * @date 2021/9/29 2:55 下午
 */
@Configuration
@MapperScan("${mybatis-plus.mapperPackage}")
public class MybatisPlusAutoConfiguration {

    @Bean
    public DbReadWriteProperties dbReadWriteProperties() {
        return new DbReadWriteProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "osc.db-read-write.enable", havingValue = "true")
    public AutoReadWriteSeparateInterceptor readWriteAutoRoutingPlugin(DbReadWriteProperties dbReadWriteProperties) {
        return new AutoReadWriteSeparateInterceptor(dbReadWriteProperties);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor plusInterceptor = new MybatisPlusInterceptor();
        //分页插件
        plusInterceptor.addInnerInterceptor(paginationInnerInterceptor());
        return plusInterceptor;
    }

    /**
     * 分页插件，自动识别数据库类型
     * https://baomidou.com/guide/interceptor-pagination.html
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * sql注入器配置
     * https://baomidou.com/guide/sql-injector.html
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new BatchInsert());
                return methodList;
            }
        };
    }

}
