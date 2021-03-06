package io.framework.myBatis.query;

import io.framework.myBatis.query.sqlSnippet.SqlSnippet;
import lombok.Data;

import java.util.List;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/9 23:03 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Data
public class SqlSnippetManager {

    List<SqlSnippet> sqlWhereSnippetList;

    List<SqlSnippet> sqlHavingSnippetList;
}