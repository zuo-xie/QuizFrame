package io.framework.myBatis.injector.methods;

import mybatis.frame.comment.ColumnInfo;
import mybatis.frame.comment.TableInfo;
import mybatis.frame.comment.enums.MySqlDML;
import mybatis.frame.injector.AbstractMethod;
import mybatis.frame.injector.SqlMethodEnums;
import mybatis.frame.util.MyBatisStringPool;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/27 21:48 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public class UpdateOne extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums updateOne = SqlMethodEnums.UPDATE_ONE;
        List<ColumnInfo> columnInfoList = tableInfo.getColumn();
        String sql = String.format(updateOne.getSql(), tableInfo.getTableName(), tableInfo.convertTrim(
                null, null, null, MyBatisStringPool.E_COMMA.getType(), MySqlDML.UPDATE, (sb) -> {
                    columnInfoList.forEach(v -> {
                        String columnName = v.getColumnName();
                        String fieId = v.getFieId();
                        sb.append(tableInfo.convertIf(fieId
                                + MyBatisStringPool.SPACE.getType()
                                + MyBatisStringPool.NOT_NULL.getType(), () -> new StringBuilder(columnName)
                                .append(MyBatisStringPool.SPACE.getType())
                                .append(MyBatisStringPool.EQUAL.getType())
                                .append(MyBatisStringPool.NUMBER_SIGN.getType())
                                .append(MyBatisStringPool.LEFT_BRACE.getType())
                                .append(fieId)
                                .append(MyBatisStringPool.RIGHT_BRACE.getType())
                                .append(MyBatisStringPool.E_COMMA.getType())));
                    });
                    return sb;
                }
        ),tableInfo.getPerKey().getColumnName(),tableInfo.getPerKey().getFieId());
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
        return this.addUpdateMappedStatement(mapperClass, tableInfo.getEntityType(),
                updateOne.getMethod(), sqlSource);
    }
}