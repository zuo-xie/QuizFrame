package io.framework.myBatis.annotations;

import io.framework.myBatis.type.SqlDataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  mybatis 字段声明
 * Description:  
 * date: 2020/10/18 13:49 
 *
 * @author zuo
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * @return 字段名
     */
    String columnName() default "";

    /**
     * @return 字段类型
     */
    SqlDataType type() default SqlDataType.NULL;

    /**
     * @return 是否可空
     */
    String notNull() default "";

    /**
     * @return 字段描述
     */
    String comment() default "";

    /**
     * @return 长度
     */
    String length() default "";

    /**
     * @return 默认值
     */
    String def() default "";
}
