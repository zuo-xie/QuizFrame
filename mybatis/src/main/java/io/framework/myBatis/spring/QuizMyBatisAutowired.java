package io.framework.myBatis.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 此类作为默认类配置类
 * Description:  
 * date: 2021/4/11 19:59 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Configurable
@Slf4j
@ConditionalOnClass({SqlSessionFactory.class,QuizSqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(QuizMyBatisProperties.class)
//在指定自动配置类结束后，调用该类
@AutoConfigureAfter({DataSourceAutoConfiguration.class,
        MybatisLanguageDriverAutoConfiguration.class})
public class QuizMyBatisAutowired implements InitializingBean {

    /**
     * 配置属性
     */
    private QuizMyBatisProperties properties;


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void checkConfigFileExists() {

    }

    /**
     * 自动注入原理
     * @param dataSource ：数据源
     * @return 返回session工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        if (properties.getCache() != null) {
            sqlSessionFactoryBean.setCache(properties.getCache());
        }
        if (properties.getConfigLocation() != null) {
            sqlSessionFactoryBean.setConfigLocation(properties.getConfigLocation());
        }
        if (properties.getConfigurationProperties() != null) {
            sqlSessionFactoryBean.setConfigurationProperties(properties.getConfigurationProperties());
        }
        if (properties.getMapperLocations() != null) {
            sqlSessionFactoryBean.setMapperLocations(properties.getMapperLocations());
        }
        if (properties.getPlugins() != null) {
            sqlSessionFactoryBean.setPlugins(properties.getPlugins());
        }
        if (properties.getDatabaseIdProvider() != null) {
            sqlSessionFactoryBean.setDatabaseIdProvider(properties.getDatabaseIdProvider());
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            sqlSessionFactoryBean.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (this.properties.getTypeAliasesSuperType() != null) {
            sqlSessionFactoryBean.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            sqlSessionFactoryBean.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }

        return sqlSessionFactoryBean.getObject();
    }
}