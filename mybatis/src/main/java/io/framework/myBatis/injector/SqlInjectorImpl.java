package io.framework.myBatis.injector;

import io.framework.myBatis.comment.TableInfo;
import io.framework.myBatis.exception.QuizMyBatisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.util.List;
import java.util.Objects;

/**
 * 一些声明信息
 * Description:  
 * date: 2021/2/10 16:33 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Slf4j
public abstract class SqlInjectorImpl implements SqlInjector {
    @Override
    public void inspectInject(Class<?> mapperClass, MapperBuilderAssistant builderAssistant,
                              List<Class<?>> genericList, TableInfo tableInfo) throws Exception {
        //如果泛型不存在抛错
        if (!genericList.isEmpty()) {
            List<AbstractMethod> methodList = getMethodList(mapperClass);
            //获取需要注入的方法
            if (!methodList.isEmpty() && tableInfo != null) {
                methodList.stream().filter(Objects::nonNull).forEach(v -> v.inject(builderAssistant,genericList.get(0),tableInfo));
            }
        } else {
            new QuizMyBatisException("注入异常");
        }
    }

    public abstract List<AbstractMethod> getMethodList(Class<?> mapperClass);
}