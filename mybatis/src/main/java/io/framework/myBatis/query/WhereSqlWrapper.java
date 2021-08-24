package io.framework.myBatis.query;

import io.framework.myBatis.query.sqlSnippet.SqlSnippet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/5 19:24 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public interface WhereSqlWrapper<Children,ColumnType> {

    List<SqlSnippet> sqlWhereSnippetList = new ArrayList<>();

    Children eq(ColumnType columnType, Object value);

    Children in(ColumnType columnType, Collection<?> value);

    Children like(ColumnType columnType,Object value);

    Children between(ColumnType columnType, Date d1 ,Date d2);

    default List<SqlSnippet> getList() {
        return this.sqlWhereSnippetList;
    }

    default void addList(SqlSnippet sqlSnippet) {
        sqlWhereSnippetList.add(sqlSnippet);
    }

    default void delList() {
        sqlWhereSnippetList.clear();
    }
}