package io.framework.myBatis.injector.methods;


import mybatis.frame.comment.ColumnInfo;
import mybatis.frame.comment.TableInfo;
import mybatis.frame.comment.enums.MySqlDML;
import mybatis.frame.injector.AbstractMethod;
import mybatis.frame.injector.SqlMethodEnums;
import mybatis.frame.util.MyBatisStringPool;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

public class InsertOne extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums insert = SqlMethodEnums.INSERT_ONE;
        List<ColumnInfo> columnInfoList = tableInfo.getColumn();
        String sql = String.format(insert.getSql(),
                tableInfo.getTableName(),
                tableInfo.convertTrim(
                        MyBatisStringPool.E_LEFT_BRACKETS.getType(),
                        MyBatisStringPool.E_RIGHT_BRACKETS.getType(),
                        null,
                        MyBatisStringPool.E_COMMA.getType(),
                        MySqlDML.INSERT,
                        (sb) -> {
                            columnInfoList.forEach(v -> {
                                String columnName = v.getColumnName();
                                String fieId = v.getFieId();
                                sb.append(tableInfo.convertIf(fieId
                                                + MyBatisStringPool.SPACE.getType()
                                                + MyBatisStringPool.NOT_NULL.getType(),
                                        () -> new StringBuilder(columnName).append(MyBatisStringPool.E_COMMA.getType())));
                            });
                            return sb;
                        }),
                tableInfo.convertTrim(
                        MyBatisStringPool.SPACE.getType()
                                + MyBatisStringPool.E_LEFT_BRACKETS.getType(),
                        MyBatisStringPool.E_RIGHT_BRACKETS.getType(),
                        null,
                        MyBatisStringPool.E_COMMA.getType(),
                        MySqlDML.INSERT,
                        (sb) -> {
                            columnInfoList.forEach(v -> {
                                String fieId = v.getFieId();
                                sb.append(tableInfo.convertIf(fieId
                                                + MyBatisStringPool.SPACE.getType()
                                                + MyBatisStringPool.NOT_NULL.getType(),
                                        () -> new StringBuilder()
                                                .append(MyBatisStringPool.NUMBER_SIGN.getType())
                                                .append(MyBatisStringPool.LEFT_BRACE.getType())
                                                .append(fieId)
                                                .append(MyBatisStringPool.RIGHT_BRACE.getType())
                                                .append(MyBatisStringPool.E_COMMA.getType())));
                            });
                            return sb;
                        }
                )
        );
        SqlSource sqlSource = this.languageDriver.createSqlSource(configuration, sql, tableInfo.getEntityType());
        //TODO：主键列需要修改
        return this.addSelectMappedStatementForOther(mapperClass, tableInfo.getEntityType(),
                insert.getMethod(), sqlSource, new NoKeyGenerator(),
                tableInfo.getPerKey().getColumnName(), "");
    }
}
