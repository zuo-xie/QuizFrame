package io.framework.myBatis.type;

import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

/**
 * MySQL重连
 * Description:  
 * date: 2020/10/21 22:20 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Getter
public enum SqlDataType {

    //int类型
    INT("INT", JdbcType.INTEGER),
    VARCHAR("VARCHAR",JdbcType.VARCHAR),
    SMALLINT("SMALLINT",JdbcType.SMALLINT),
    TIME("TIME",JdbcType.TIMESTAMP),
    DATETIME("DATETIME",JdbcType.TIMESTAMP),
    NULL("",JdbcType.NULL);

    private final String type;
    private final JdbcType jdbcType;

    SqlDataType(String type,JdbcType jdbcType) {
        this.type = type;
        this.jdbcType = jdbcType;
    }

}
