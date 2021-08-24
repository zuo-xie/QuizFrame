package io.framework.myBatis.query.sqlSnippet;

import lombok.Data;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/13 1:04 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Data
public class WhereSqlSnippet implements SqlSnippet {

    private String sqlField;

    private String keyword;

    private Object javaValue;
}