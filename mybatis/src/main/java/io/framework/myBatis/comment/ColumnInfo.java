package io.framework.myBatis.comment;

import lombok.Data;

/**
 * 表结构对应字段
 * @author zuo
 */
@Data
public class ColumnInfo {
    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列类型
     */
    private String types;

    /**
     * 字段长度
     */
    private String length;

    /**
     * 是否为空
     */
    private String isNull;

    /**
     * 是否为主键
     */
    private Boolean primaryKey;

    /**
     * 描述
     */
    private String comment;

    /**
     * 对应字段名称
     */
    private String fieId;
}
