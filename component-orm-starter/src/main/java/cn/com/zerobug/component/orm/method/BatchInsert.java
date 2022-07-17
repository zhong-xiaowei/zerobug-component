package cn.com.zerobug.component.orm.method;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

/**
 * dao层 批量插入sql
 *
 * @author zhongxiaowei
 */
@SuppressWarnings("serial")
public class BatchInsert extends AbstractMethod {

    private final static String[] FILL_PROPERTY = {"createTime", "createBy", "updateTime", "updateBy"};
    private final static String   METHOD_NAME   = "batchInsert";

    public BatchInsert() {
        super(METHOD_NAME);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        final String insertSql    = "<script>insert into %s %s values %s</script>";
        final String fieldSql     = prepareTableField(tableInfo);
        final String valueSql     = prepareTableFieldValues(tableInfo);
        KeyGenerator keyGenerator = new NoKeyGenerator();
        String       keyProperty  = null;
        String       keyColumn    = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StrUtil.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /** 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(METHOD_NAME, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        final String sqlResult = String.format(insertSql, tableInfo.getTableName(), fieldSql, valueSql);
        SqlSource    sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, METHOD_NAME, sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    /**
     * 准备表字段 sql
     *
     * @param tableInfo 表信息
     * @return
     */
    private String prepareTableField(TableInfo tableInfo) {
        StringBuilder field = new StringBuilder();
        // 主键列处理
        if (StrUtil.isNotBlank(tableInfo.getKeyColumn())) {
            field.append(tableInfo.getKeyColumn()).append(",");
        }
        tableInfo.getFieldList().forEach(table -> field.append(table.getColumn()).append(","));
        // 去除最后的逗号
        field.delete(field.length() - 1, field.length());
        // 用括号 括起来
        field.insert(0, "(");
        field.append(")");
        return field.toString();
    }

    /**
     * 准备表字段值 sql
     *
     * @param tableInfo
     * @return
     */
    private String prepareTableFieldValues(TableInfo tableInfo) {
        final StringBuilder values = new StringBuilder();
        values.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        // 主键列处理
        if (StrUtil.isNotBlank(tableInfo.getKeyColumn())) {
            values.append("\n#{item.").append(tableInfo.getKeyProperty()).append("},\n");
        }
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        int                  last      = fieldList.size() - 1;
        for (int i = 0; i < fieldList.size(); i++) {
            String property = fieldList.get(i).getProperty();
            if (!StrUtil.equalsAny(property, FILL_PROPERTY)) {
                values.append("<if test=\"item.").append(property).append(" != null\">");
                values.append("#{item.").append(property).append("}");
                if (i != last) {
                    values.append(",");
                }
                values.append("</if>");
                values.append("<if test=\"item.").append(property).append(" == null\">");
                values.append("DEFAULT");
                if (i != last) {
                    values.append(",");
                }
                values.append("</if>");
            } else {
                values.append("#{item.").append(property).append("}");
                if (i != last) {
                    values.append(",");
                }
            }
        }
        values.append("</foreach>");
        return values.toString();
    }


}