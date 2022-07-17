package cn.com.zerobug.component.datapermission.core;

import cn.com.zerobug.component.datapermission.DataPermissionContextReceiver;
import cn.com.zerobug.component.datapermission.DataPermissionHandler;
import cn.com.zerobug.component.datapermission.annotation.DataPermission;
import cn.com.zerobug.component.datapermission.utils.DataPermissionContextHolder;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhongxiaowei
 * @date 2022/2/28
 */
@Slf4j
public class DefaultDataPermissionSqlResolver extends JsqlParserSupport implements DataPermissionSqlResolver {

    private       List<DataPermissionHandler>   handlers;
    private final DataPermissionContextReceiver receiver;

    public DefaultDataPermissionSqlResolver(List<DataPermissionHandler> handlers, DataPermissionContextReceiver receiver) {
        this.handlers = handlers;
        this.receiver = receiver;
    }

    @Override
    public void preprocessing(BoundSql sql, MappedStatement mappedStatements, SqlCommandType sqlCommandType) {
        // 暂时只对 select 进行数据过滤
        DataPermission dataPermission;
        if (sqlCommandType == SqlCommandType.SELECT
                && (dataPermission = DataPermissionContextHolder.get()) != null) {
            DataPermissionsContext.AnnotationProperties annotationProperties = buildAnnotationProperties(dataPermission);
            if (annotationProperties.getExcludeStatementIds()
                    .stream().anyMatch(Predicate.isEqual(mappedStatements.getId()))) {
                return;
            }
            DataPermissionsContext context = buildContext(annotationProperties);
            this.parserMulti(sql.getSql(), mappedStatements.getId(), context);
        }
    }

    public static DataPermissionsContext.AnnotationProperties buildAnnotationProperties(DataPermission dataPermission) {
        DataPermissionsContext.AnnotationProperties annotationProperties = new DataPermissionsContext.AnnotationProperties();

        List<DataPermissionsContext.AnnotationTableField> tableFields = Arrays.stream(Optional.of(dataPermission.fields()).orElseThrow(() -> new IllegalArgumentException(" No table fields ")))
                .map(field -> new DataPermissionsContext.AnnotationTableField().setField(field.field()).setAlias(field.alias()))
                .collect(Collectors.toList());

        List<String> excludeStatementIds = Arrays.stream(dataPermission.exclude())
                .map(item -> new StringBuilder(item.clazz().getName())
                        .append(StrPool.DOT)
                        .append(item.method()).toString())
                .collect(Collectors.toList());

        annotationProperties.setTableFields(tableFields);
        annotationProperties.setExcludeStatementIds(excludeStatementIds);
        annotationProperties.setDisabledHandlerClasses(dataPermission.disable());
        annotationProperties.setCustom(dataPermission.custom());
        return annotationProperties;
    }

    private DataPermissionsContext buildContext(DataPermissionsContext.AnnotationProperties properties) {
        DataPermissionsContext receive = receiver.receive();
        receive.setAnnotationProperties(properties);
        return receive;
    }

    protected void processSelect(Select select, int index, String sql, Object obj, DataPermissionsContext context) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            doProcessSelect(index, context, (PlainSelect) selectBody);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selects = setOperationList.getSelects();
            selects.forEach(item -> doProcessSelect(index, context, (PlainSelect) selectBody));
        } else {
            // WITH AS 语句不支持 一般写了该类型语句不建议做数据权限，建议手写过滤
            // VALUES 语句不支持 SELECT 正常情况不会出现VALUES
            throw new UnsupportedOperationException("This SQL does not support data permission");
        }
    }

    protected String parserMulti(String sql, Object obj, DataPermissionsContext context) {
        if (log.isDebugEnabled()) {
            log.debug("original SQL: [ {} ]", sql);
            log.debug("start parse.....");
        }
        try {
            StringBuilder sb = new StringBuilder();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (i > 0) {
                    sb.append(StringPool.SEMICOLON);
                }
                sb.append(processParser(statement, i, sql, obj, context));
                i++;
            }
            return sb.toString();
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), sql);
        }
    }

    protected String processParser(Statement statement, int index, String sql, Object obj, DataPermissionsContext context) {
        if (log.isDebugEnabled()) {
            log.debug("SQL to parse, index：[ {} ], SQL: [ {} ]", index, sql);
            log.debug("Start select SQL processing....");
        }
        this.processSelect((Select) statement, index, sql, obj, context);
        sql = statement.toString();
        if (log.isDebugEnabled()) {
            log.debug("processing completed SQL: [ {} ]", sql);
        }
        return sql;
    }

    private void doProcessSelect(int index, DataPermissionsContext context, PlainSelect selectBody) {
        giveToHandler(handler -> {
            if (handler.matching(context)) {
                handler.handleSelectExpressions(selectBody, index, context);
                if (log.isDebugEnabled()) {
                    log.debug("[{}] processor completed execution，SQL: [{}]", handler.getClass().getSimpleName(), selectBody.toString());
                }
            }
        });
    }

    protected void giveToHandler(Consumer<DataPermissionHandler> consumer) {
        this.handlers.forEach(item -> consumer.accept(item));
    }

}
