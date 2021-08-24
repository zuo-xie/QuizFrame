package io.framework.myBatis.injector.methods;

import io.framework.myBatis.comment.TableInfo;
import io.framework.myBatis.injector.AbstractMethod;
import io.framework.myBatis.injector.SqlMethodEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/10 17:02 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Slf4j
public class SelectById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception {
        SqlMethodEnums sqlMethodEnums = SqlMethodEnums.SELECT_BY_ID;
        String resultMapName = tableInfo.getResultMapName();
        RawSqlSource rawSqlSource = new RawSqlSource(this.configuration, String.format(sqlMethodEnums.getSql(),
                this.sqlSelectColumns(tableInfo), tableInfo.getTableName(),
                tableInfo.getPerKey().getColumnName(),
                tableInfo.getPerKey().getFieId()), Object.class);
        return this.addSelectMappedStatement(mapperClass, sqlMethodEnums.getMethod(), rawSqlSource, tableInfo, resultMapName);
    }
}