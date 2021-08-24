package io.framework.myBatis.query;

import lombok.Getter;


/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/9 21:35 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Getter
public enum MyBatisKeyword {

    EQ("%s = %s", SqlType.WHERE),
    IN("%s in (%s)", SqlType.WHERE),
    LIMIT("limit %s", SqlType.HAVING),
    LIKE("%s like '%%s%'", SqlType.WHERE),
    BETWEEN("%s between %s", SqlType.WHERE);

    private final String sqlField;
    private final SqlType type;

    MyBatisKeyword(String sqlField, SqlType type) {
        this.sqlField = sqlField;
        this.type = type;
    }
}
