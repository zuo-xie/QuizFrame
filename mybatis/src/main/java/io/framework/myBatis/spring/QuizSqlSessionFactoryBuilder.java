package io.framework.myBatis.spring;

import io.framework.myBatis.core.QuizXMLConfigBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.Properties;

public class QuizSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    /**
     * 自定义的build模式
     * 该模式去调用自定义的Xml配置解析
     * @param reader 读取配置流
     * @param environment 数据源
     * @param properties 属性配置文件
     * @return SqlSessionFactory
     */
    @Override
    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        QuizXMLConfigBuilder quizXMLConfigBuilder = new QuizXMLConfigBuilder(reader, environment, properties);
        return build(quizXMLConfigBuilder.parse());
    }
}
