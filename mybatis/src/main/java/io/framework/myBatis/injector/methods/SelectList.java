package io.framework.myBatis.injector.methods;

import mybatis.frame.comment.TableInfo;
import mybatis.frame.injector.AbstractMethod;
import mybatis.frame.injector.SqlMethodEnums;
import mybatis.frame.util.MyBatisStringPool;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class SelectList extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums selectList = SqlMethodEnums.SELECT_LIST;
        String sql = String.format(selectList.getSql(),
                this.sqlSelectColumns(tableInfo),
                tableInfo.getTableName(),
                tableInfo.convertIf(MyBatisStringPool.SQL_WRAPPER.getType() +
                        MyBatisStringPool.SPACE.getType() + MyBatisStringPool.NOT_NULL.getType(), () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(tableInfo.convertWhere(() -> {
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append(tableInfo.convertIf(MyBatisStringPool.SQL_WRAPPER.getType() + MyBatisStringPool.NOT_NULL.getType(),() -> {
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
        return this.addSelectMappedStatementForOther(mapperClass,selectList.getMethod(),sqlSource,tableInfo.getEntityType(),tableInfo.getResultMapName());
    }
}
