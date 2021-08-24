package io.framework.myBatis.query;

import mybatis.frame.query.sqlSnippet.SqlSnippet;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于不需要 指定数据库字段的SQL语句
 * Description:  
 * date: 2021/3/9 23:00 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public interface HavingSqlWrapper<Children,ColumnType> {

    List<SqlSnippet> sqlHavingSnippetList = new ArrayList<>();

    default List<SqlSnippet> getList() {
        return this.sqlHavingSnippetList;
    }

    default void addList(SqlSnippet sqlSnippet) {
        sqlHavingSnippetList.add(sqlSnippet);
    }

    default void delList() {
        sqlHavingSnippetList.clear();
    }

    Children limit(Integer start,Integer pageSize);

}
