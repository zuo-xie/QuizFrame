package io.framework.myBatis.injector.methods;

import mybatis.frame.comment.TableInfo;
import mybatis.frame.injector.AbstractMethod;
import mybatis.frame.injector.SqlMethodEnums;
import mybatis.frame.util.MyBatisStringPool;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/17 21:19 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public class Count extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums count = SqlMethodEnums.COUNT;
        String sql = String.format(count.getSql(), tableInfo.getTableName(), tableInfo.convertIf(MyBatisStringPool.SQL_WRAPPER.getType() +
                MyBatisStringPool.SPACE.getType() + MyBatisStringPool.NOT_NULL.getType(), () -> {
            StringBuilder sb = new StringBuilder();
            sb.append(tableInfo.convertWhere(() -> {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(tableInfo.convertIf(MyBatisStringPool.SQL_WRAPPER.getType() + MyBatisStringPool.NOT_NULL.getType(), () -> {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(MyBatisStringPool.USD.getType());
                    sb2.append(MyBatisStringPool.LEFT_BRACE.getType());
                    sb2.append(MyBatisStringPool.SQL_WRAPPER.getType());
                    sb2.append(MyBatisStringPool.POINT.getType());
                    sb2.append(MyBatisStringPool.SQL_WRAPPER.getType());
                    sb2.append(MyBatisStringPool.RIGHT_BRACE.getType());
                    return sb2;
                }));
                return sb1;
            }));
            return sb;
        }));
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
        tableInfo.setEntityType(Integer.class);
        return this.addSelectMappedStatement(mapperClass, count.getMethod(), sqlSource, tableInfo, null);
    }
}