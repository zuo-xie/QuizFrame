package io.framework.myBatis.spring;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mybatis.frame.config.ConfigManage;
import mybatis.frame.core.QuizXMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * quiz框架的sqlSession工厂
 * Description:  
 * date: 2021/2/20 20:05 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Slf4j
@Data
public class QuizSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>,
        InitializingBean, ApplicationListener<ApplicationEvent> {

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

    private String environment = SqlSessionFactoryBean.class.getSimpleName();

    private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    /**
     * 用来创建Bean的方法 Bean为SqlSessionFactory
     *
     * @return 返回生成的 sqlSessionFactory
     * @throws Exception 异常
     */
    @Override
    public SqlSessionFactory getObject() throws Exception {
        //如果初始化Bean失败，手动初始化
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    /**
     * Bean初始化方法  初始化  SqlSessionFactory
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }

    /**
     * 创建sqlSessionFactory
     *
     * @return sqlSessionFactory
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
        final Configuration targetConfiguration;

        QuizXMLConfigBuilder quizXMLConfigBuilder = null;
        if (!(this.quizConfiguration == null)) {
            targetConfiguration = this.quizConfiguration;
            if (targetConfiguration.getVariables() == null) {
                targetConfiguration.setVariables(configurationProperties);
            } else if (this.configurationProperties != null) {
                targetConfiguration.getVariables().putAll(configurationProperties);
            }
        } else if (this.configLocation != null) {
            quizXMLConfigBuilder = new QuizXMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
            targetConfiguration = quizXMLConfigBuilder.getConfiguration();
        } else {
            log.debug("quizConfiguration 未被注入 使用默认 Configuration");
            targetConfiguration = ConfigManage.getInstance().getConfiguration();
            Optional.ofNullable(this.configurationProperties).ifPresent(targetConfiguration::setVariables);
        }


        Optional.ofNullable(this.objectFactory).ifPresent(targetConfiguration::setObjectFactory);
        Optional.ofNullable(this.objectWrapperFactory).ifPresent(targetConfiguration::setObjectWrapperFactory);
        Optional.ofNullable(this.vfs).ifPresent(targetConfiguration::setVfsImpl);

        if (hasLength(this.typeAliasesPackage)) {
            scanClasses(this.typeAliasesPackage, this.typeAliasesSuperType).stream()
                    .filter(clazz -> !clazz.isAnonymousClass()).filter(clazz -> !clazz.isInterface())
                    .filter(clazz -> !clazz.isMemberClass()).forEach(targetConfiguration.getTypeAliasRegistry()::registerAlias);
        }

        if (!isEmpty(this.typeAliases)) {
            Stream.of(this.typeAliases).forEach(typeAlias -> {
                targetConfiguration.getTypeAliasRegistry().registerAlias(typeAlias);
                log.debug("注册别名为 ： '{}' ", typeAlias);

            });
        }

        if (!isEmpty(this.plugins)) {
            Stream.of(this.plugins).forEach(plugin -> {
                targetConfiguration.addInterceptor(plugin);
                log.debug("注册插件 ： '{}' ", plugin);
            });
        }

        if (hasLength(this.typeHandlersPackage)) {
            scanClasses(this.typeHandlersPackage, TypeHandler.class).stream().filter(clazz -> !clazz.isAnonymousClass())
                    .filter(clazz -> !clazz.isInterface()).filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                    .forEach(targetConfiguration.getTypeHandlerRegistry()::register);
        }

        if (!isEmpty(this.typeHandlers)) {
            Stream.of(this.typeHandlers).forEach(typeHandler -> {
                targetConfiguration.getTypeHandlerRegistry().register(typeHandler);
                log.debug("注册类型处理器 ：'{}' ", typeHandler);
            });
        }

        targetConfiguration.setDefaultEnumTypeHandler(defaultEnumTypeHandler);

        if (!isEmpty(this.scriptingLanguageDrivers)) {
            Stream.of(this.scriptingLanguageDrivers).forEach(languageDriver -> {
                targetConfiguration.getLanguageRegistry().register(languageDriver);
                log.debug("Registered scripting language driver: '{}' ", languageDriver);
            });
        }
        Optional.ofNullable(this.defaultScriptingLanguageDriver)
                .ifPresent(targetConfiguration::setDefaultScriptingLanguage);

        if (this.databaseIdProvider != null) {// fix #64 set databaseId before parse mapper xmls
            try {
                targetConfiguration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }

        Optional.ofNullable(this.cache).ifPresent(targetConfiguration::addCache);

        if (quizXMLConfigBuilder != null) {
            try {
                quizXMLConfigBuilder.parse();
                log.debug("解析的配置文件 ： '{}'", configLocation);
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        targetConfiguration.setEnvironment(new Environment(this.environment,
                this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory,
                this.dataSource));

        if (this.mapperLocations != null) {
            if (this.mapperLocations.length == 0) {
                log.warn("指定了属性“ mapperLocations”，但找不到匹配的资源。");
            } else {
                for (Resource mapperLocation : this.mapperLocations) {
                    if (mapperLocation == null) {
                        continue;
                    }
                    try {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
                                targetConfiguration, mapperLocation.toString(), targetConfiguration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    } catch (Exception e) {
                        throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                    } finally {
                        ErrorContext.instance().reset();
                    }
                    log.debug("解析的映射文件 ：'{}'", mapperLocation);
                }
            }
        } else {
            log.debug("为指定属性  mapperLocations");
        }
        return new QuizSqlSessionFactoryBuilder().build(targetConfiguration);
    }

    private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        String[] packagePatternArray = tokenizeToStringArray(packagePatterns,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String packagePattern : packagePatternArray) {
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
            for (Resource resource : resources) {
                try {
                    ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                    Class<?> clazz = Resources.classForName(classMetadata.getClassName());
                    if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
                        classes.add(clazz);
                    }
                } catch (Throwable e) {
                    log.warn("无法加载 ' {}'. 报错原因 {}", resource, e.toString());
                }
            }
        }
        return classes;
    }
}