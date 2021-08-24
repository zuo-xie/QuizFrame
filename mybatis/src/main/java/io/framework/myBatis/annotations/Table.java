package io.framework.myBatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一些声明信息
 * Description:  
 * date: 2020/10/17 15:12 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * 表名
     */
    String tableName() default "";

    /**
     * 表描述
     */
    String comment() default "";

    /**
     * 字符集
     */
    String charset() default "UTF8";
}
