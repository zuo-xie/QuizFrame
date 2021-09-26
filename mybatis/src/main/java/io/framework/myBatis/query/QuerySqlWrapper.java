package io.framework.myBatis.query;

import io.framework.myBatis.annotations.Column;
import io.framework.myBatis.function.SFuntion;
import io.framework.myBatis.query.sqlSnippet.HavingSqlSnippet;
import io.framework.myBatis.query.sqlSnippet.SqlSnippet;
import io.framework.myBatis.query.sqlSnippet.WhereSqlSnippet;
import io.framework.myBatis.type.MyBatisStringPool;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/9 22:43 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Slf4j
public class QuerySqlWrapper<T> extends AbsSqlWrapper<T, SFuntion<T, ?>, QuerySqlWrapper<T>> implements SqlWrapper {

    @Override
    public String getKeyword(SFuntion<T, ?> tsFuntion) {
        String sqlField = "";
        try {
            String implClass = tsFuntion.getImplClass();

            implClass = implClass.replace("/", ".");
            Class<?> aClass = Class.forName(implClass);
            //TODO:改
            String implMethodName = tsFuntion.getImplMethodName();
            StringBuilder methodName = new StringBuilder(implMethodName);
            methodName.delete(0, 3);
            implMethodName = methodName.toString();
            char[] chars = implMethodName.toCharArray();
            chars[0] += 32;
            Field field = aClass.getDeclaredField(String.valueOf(chars));
            Set<Annotation> annotationSet = Arrays.stream(field.getDeclaredAnnotations())
                    .filter(v -> v.annotationType().getName().equals(Column.class.getName()))
                    .collect(Collectors.toSet());
            for (Annotation annotation : annotationSet) {
                Column column = (Column) annotation;
                sqlField = column.columnName();
            }

        } catch (Exception e) {
            return "";
        }
        return sqlField;
    }

    @Override
    public String getSqlWrapper() {
        SqlSnippetManager listManager = super.getListManager();
        StringBuilder sqlWrapper = new StringBuilder();
        this.splicingSqlWhere(listManager.getSqlWhereSnippetList(), sqlWrapper);
        this.splicingSqlHaving(listManager.getSqlHavingSnippetList(), sqlWrapper);
        super.delList();
        return sqlWrapper.toString();
    }

    private void splicingSqlWhere(List<SqlSnippet> list, StringBuilder sb) {
        if (!list.isEmpty()) {
            list.forEach(v -> {
                WhereSqlSnippet v1 = (WhereSqlSnippet) v;
                sb.append(String.format(v1.getKeyword(), v1.getSqlField(), v1.getJavaValue()));
                sb.append(MyBatisStringPool.WRAP.getType())
                        .append(MyBatisStringPool.AND.getType())
                        .append(MyBatisStringPool.WRAP.getType());
            });
            sb.replace(sb.length() - 5, sb.length(), MyBatisStringPool.SPACE.getType());
        }

    }

    private void splicingSqlHaving(List<SqlSnippet> list, StringBuilder sb) {
        if (!list.isEmpty()) {
            list.forEach(v -> {
                HavingSqlSnippet v1 = (HavingSqlSnippet) v;
                sb.append(String.format(v1.getKeyword(), v1.getValue()));
                sb.append(MyBatisStringPool.SPACE.getType()).append(MyBatisStringPool.E_COMMA.getType());
            });
            sb.replace(sb.length() - 1, sb.length(), MyBatisStringPool.SPACE.getType());
        }

    }
}