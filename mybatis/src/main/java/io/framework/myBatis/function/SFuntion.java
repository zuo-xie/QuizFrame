package io.framework.myBatis.function;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/3/6 17:12 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@FunctionalInterface
public interface SFuntion<T, R> extends Serializable {

    R get(T t);

    default SerializedLambda getSerializedLambda() throws Exception {
        Method writeReplace = this.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        return (SerializedLambda) writeReplace.invoke(this);
    }

    default String getImplClass() throws Exception {
        return getSerializedLambda().getImplClass();
    }

    default String getImplMethodName() throws Exception {
        return getSerializedLambda().getImplMethodName();
    }
}