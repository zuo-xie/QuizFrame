package io.framework.myBatis.binding;

import lombok.Getter;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * copy
 * Description:  
 * date: 2021/2/20 15:51 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Getter
public class QuizMapperProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, QuizMapperProxy.MapperMethodInvoker> methodCache = new ConcurrentHashMap<>();

    public QuizMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(QuizMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        final QuizMapperProxy<T> mapperProxy = new QuizMapperProxy<>(sqlSession, mapperInterface, methodCache);
        return newInstance(mapperProxy);
    }
}