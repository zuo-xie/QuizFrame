package io.framework.myBatis.query;


import io.framework.myBatis.query.sqlSnippet.HavingSqlSnippet;
import io.framework.myBatis.query.sqlSnippet.SqlSnippet;
import io.framework.myBatis.query.sqlSnippet.WhereSqlSnippet;
import io.framework.myBatis.type.MyBatisStringPool;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/5 19:31 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@SuppressWarnings("all")
public abstract class AbsSqlWrapper<T, ColumnType, Children extends AbsSqlWrapper<T, ColumnType, Children>>
        implements WhereSqlWrapper<Children, ColumnType>, HavingSqlWrapper<Children, ColumnType> {

    public Children eq(ColumnType columnType, Object value) {
        return this.addCondition(columnType, MyBatisKeyword.EQ, value);
    }

    @Override
    public Children in(ColumnType columnType, Collection<?> value) {
        return this.addCondition(columnType, MyBatisKeyword.IN, StringUtils.join((Collection) value, MyBatisStringPool.E_COMMA.getType()));
    }

    @Override
    public Children limit(Integer start, Integer pageSize) {
        Object value = start + MyBatisStringPool.E_COMMA.getType() + pageSize;
        return this.addCondition(null, MyBatisKeyword.LIMIT, value);
    }

    @Override
    public Children like(ColumnType columnType, Object value) {
        return this.addCondition(columnType, MyBatisKeyword.LIKE, value);
    }

    @Override
    public Children between(ColumnType columnType, Date d1, Date d2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String value = MyBatisStringPool.APOSTROPHE.getType() +  df.format(d1) +  MyBatisStringPool.APOSTROPHE.getType()
                + MyBatisStringPool.AND.getType() +  MyBatisStringPool.APOSTROPHE.getType() + df.format(d2) +
                MyBatisStringPool.APOSTROPHE.getType();
        return this.addCondition(columnType, MyBatisKeyword.BETWEEN, value);
    }

    @Override
    public List<SqlSnippet> getList() {
        return null;
    }

    @Override
    public void addList(SqlSnippet sqlSnippet) {

    }

    @SuppressWarnings(value = "All")
    private Children addCondition(ColumnType columnType, MyBatisKeyword keyword, Object value) {
        switch (keyword.getType()) {
            case WHERE:
                WhereSqlSnippet sqlSnippet = new WhereSqlSnippet();
                sqlSnippet.setJavaValue(value);
                sqlSnippet.setKeyword(keyword.getSqlField());
                sqlSnippet.setSqlField(this.getKeyword(columnType));
                WhereSqlWrapper.super.addList(sqlSnippet);
                break;
            case HAVING:
                HavingSqlSnippet havingSqlSnippet = new HavingSqlSnippet();
                havingSqlSnippet.setValue(value);
                havingSqlSnippet.setKeyword(keyword.getSqlField());
                HavingSqlWrapper.super.addList(havingSqlSnippet);
                break;
            case GROUP_BY:
            case ORDER_BY:
        }
        return (Children) this;
    }

    public abstract String getKeyword(ColumnType columnType);


    public SqlSnippetManager getListManager() {
        SqlSnippetManager sqlSnippetManager = new SqlSnippetManager();
        sqlSnippetManager.setSqlWhereSnippetList(WhereSqlWrapper.super.getList());
        sqlSnippetManager.setSqlHavingSnippetList(HavingSqlWrapper.super.getList());
        return sqlSnippetManager;
    }

    public void delList() {
        WhereSqlWrapper.super.delList();
        HavingSqlWrapper.super.delList();
    }
}