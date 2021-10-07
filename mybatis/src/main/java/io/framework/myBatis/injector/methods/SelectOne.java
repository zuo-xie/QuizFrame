package io.framework.myBatis.injector.methods;

import io.framework.myBatis.comment.TableInfo;
import io.framework.myBatis.injector.AbstractMethod;
import io.framework.myBatis.injector.SqlMethodEnums;
import io.framework.myBatis.type.MyBatisStringPool;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class SelectOne extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums selectOne = SqlMethodEnums.SELECT_ONE;
        final String sql = String.format(selectOne.getSql(),
                tableInfo.getAllSqlSelect(),
                tableInfo.getTableName(),
                tableInfo.builderAllIf());
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
        return this.addSelectMappedStatementForOther(mapperClass,
                selectOne.getMethod(),
                sqlSource,
                tableInfo.getEntityType(),
                tableInfo.getResultMapName());
    }
}
