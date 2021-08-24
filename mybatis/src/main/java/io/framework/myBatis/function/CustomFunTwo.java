package io.framework.myBatis.function;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/27 17:02 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@FunctionalInterface
public interface CustomFunTwo<T> {

    T method(T t);
}