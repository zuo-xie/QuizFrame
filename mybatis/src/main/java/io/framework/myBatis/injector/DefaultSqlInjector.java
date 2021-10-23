package io.framework.myBatis.injector;


import io.framework.myBatis.injector.methods.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zuo
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
                new SelectOne(),
                new Count()
        ).collect(Collectors.toList());
    }
}