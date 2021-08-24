package io.framework.myBatis.injector;


import io.framework.myBatis.injector.methods.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/13 16:04 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public class DefaultSqlInjector extends SqlInjectorImpl {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
                new SelectById(),
                new InsertOne(),
                new DeleteOne(),
                new UpdateOne(),
                new SelectList(),
                new Count()
        ).collect(Collectors.toList());
    }
}