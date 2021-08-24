package io.framework.myBatis;


import lombok.extern.slf4j.Slf4j;
import mybatis.frame.binding.QuizMapperProxyFactory;
import mybatis.frame.builder.MapperAnnotationBuilder;
import mybatis.frame.exception.QuizMyBatisException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * quiz框架自定义mapper注册器
 * Description:  
 * date: 2021/2/9 9:37 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Slf4j
public class QuizMapperRegistry extends MapperRegistry {

    private final Configuration configuration;
    private final Map<Class<?>, QuizMapperProxyFactory<?>> knownMappers = new HashMap<>();

    public QuizMapperRegistry(Configuration config) {
        super(config);
        this.configuration = config;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        //判断是否为接口
        if (type.isInterface()) {
            //是否存在
            if (hasMapper(type)) {
                log.error("{}存在", type.getName());
                return;
            }
            //用于判断是否注入完成
            boolean loadCompleted = true;
            try {
                knownMappers.put(type, new QuizMapperProxyFactory<>(type));
                MapperAnnotationBuilder mapperAnnotationBuilder = new MapperAnnotationBuilder(configuration,type);
                mapperAnnotationBuilder.parse();
                loadCompleted = false;
            } finally {
                if (loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final QuizMapperProxyFactory<T> mapperProxyFactory = (QuizMapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new QuizMyBatisException("从 QuizMapperRegistry 未查询到 Mapper代理工厂");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * knownMappers是否存在对应type的key
     *
     * @param type 类
     * @param <T>  泛型
     * @return 是否存在
     */
    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}