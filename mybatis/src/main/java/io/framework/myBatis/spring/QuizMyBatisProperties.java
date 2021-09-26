package io.framework.myBatis.spring;

import lombok.Data;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mybatis 配置属性 于spring的properties
 * 配置文件元数据
 * Description:  
 * date: 2021/4/11 19:50 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Data
@ConfigurationProperties(prefix = "quiz.mybatis.config")
public class QuizMyBatisProperties {
    private Resource configLocation;

    private Configuration quizConfiguration;

    private DataSource dataSource;

    private SqlSessionFactory sqlSessionFactory;

    private Properties configurationProperties;

    private Resource[] mapperLocations;

    private ObjectFactory objectFactory;

    private ObjectWrapperFactory objectWrapperFactory;

    private Class<? extends VFS> vfs;

    private String typeAliasesPackage;

    private Class<?> typeAliases;

    private Class<?> typeAliasesSuperType;

    private Cache cache;

    private Interceptor plugins;

    private TypeHandler<?>[] typeHandlers;

    private String typeHandlersPackage;

    @SuppressWarnings("All")
    private Class<? extends TypeHandler> defaultEnumTypeHandler;

    private LanguageDriver[] scriptingLanguageDrivers;

    private TransactionFactory transactionFactory;

    private DatabaseIdProvider databaseIdProvider;

}