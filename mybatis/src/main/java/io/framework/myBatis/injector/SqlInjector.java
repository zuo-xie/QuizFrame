package io.framework.myBatis.injector;

import io.framework.myBatis.comment.TableInfo;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.util.List;

/**
 * SQL注入器
 * Description:  
 * date: 2021/2/10 16:27 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public interface SqlInjector {

    /**
     * 检查sql是否注入
     * @param mapperClass mapper接收的class对象
     * @param builderAssistant mapper信息
     * @param genericList 泛型集合
     * @param tableInfo 表信息
     */
    void inspectInject(Class<?> mapperClass, MapperBuilderAssistant builderAssistant,
                       List<Class<?>> genericList, TableInfo tableInfo);
}
