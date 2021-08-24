package io.framework.myBatis.injector;

import lombok.Getter;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/10 17:04 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Getter
public enum SqlMethodEnums {

    SELECT_BY_ID("selectById", "根据主键Id查询", "SELECT %s FROM %s WHERE %s=#{%s}"),
    INSERT_ONE("insertOne","单个添加","<script>insert into %s %s values %s</script>"),
    DELETE_ONE("deleteOne","批量删除","DELETE FROM %s WHERE %s=#{%s}"),
    UPDATE_ONE("updateOne","单个更新","<script>update %s SET %s WHERE %s=#{%s}</script>"),

    COUNT("count","条件统计数量","<scrip> SELECT count(1) FROM %s %s</script> "),
    SELECT_LIST("selectList","查询集合","<script>SELECT %s FROM %s %s</script>");
    private final String method;
    private final String data;
    private final String sql;

    SqlMethodEnums(String method, String data, String sql) {
        this.method = method;
        this.data = data;
        this.sql = sql;
    }
}
