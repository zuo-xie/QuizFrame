package io.framework.myBatis.injector;


import io.framework.myBatis.comment.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * 作为通用sql语句的抽象类，包含注入方法
 * Description:  
 * date: 2021/2/10 16:54
 */
@Slf4j
public abstract class AbstractMethod {

    protected Configuration configuration;
    protected LanguageDriver languageDriver;
    protected MapperBuilderAssistant builderAssistant;

    /**
     * 注入自定义方法
     */
    public void inject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass, TableInfo tableInfo) {
        this.configuration = builderAssistant.getConfiguration();
        this.builderAssistant = builderAssistant;
        this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
        /* 注入自定义方法 */
        try {
            this.injectMappedStatement(mapperClass, tableInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 注入自定义 MappedStatement
     * 生成不同的sql 语句的MappedStatement
     * @param mapperClass mapper 接口
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    public abstract MappedStatement injectMappedStatement(Class<?> mapperClass, TableInfo tableInfo) throws Exception;

    /**
     * 获取所有表信息
     * @param tableInfo 表信息
     */
    protected String sqlSelectColumns(TableInfo tableInfo) throws Exception {
        return tableInfo.getAllSqlSelect();
    }

    /**
     * 添加查询方法到mapper管理器中
     *
     * @param id          mapper管理器唯一标识
     * @param mapperClass 对应实体类
     * @param table       表属性
     * @return mapper管理器
     */
    protected MappedStatement addSelectMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource,
                                                       TableInfo table,String resultMap) {
        return this.addSelectMappedStatementForOther(mapperClass, id, sqlSource, table.getEntityType(),resultMap);
    }


    /**
     * 查询
     */
    protected MappedStatement addSelectMappedStatementForOther(Class<?> mapperClass, String id, SqlSource sqlSource,
                                                               Class<?> resultType,String resultMap) {
        if (StringUtils.isNotBlank(resultMap)) {
            resultType = null;
        }
        return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.SELECT, null,
                resultMap, resultType, new NoKeyGenerator(), null, null);
    }

    /**
     * 插入
     */
    protected MappedStatement addSelectMappedStatementForOther(Class<?> mapperClass, Class<?> parameterType, String id,
                                                               SqlSource sqlSource, KeyGenerator keyGenerator,
                                                               String keyProperty, String keyColumn) {
        return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.INSERT, parameterType, null,
                Integer.class, keyGenerator, keyProperty, keyColumn);
    }


    /**
     * 更新
     */
    protected MappedStatement addUpdateMappedStatement(Class<?> mapperClass, Class<?> parameterType, String id,
                                                       SqlSource sqlSource) {
        return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.UPDATE, parameterType, null,
                Integer.class, new NoKeyGenerator(), null, null);
    }

    protected MappedStatement addDeleteMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource) {
        return addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.DELETE, null,
                null, Integer.class, new NoKeyGenerator(), null, null);
    }

    /**
     * 将sql方法添加到mapper管理器中
     * @param mapperClass 实体类
     * @param id 唯一标识
     */
    protected MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource,
                                                 SqlCommandType sqlCommandType, Class<?> parameterType,
                                                 String resultMap, Class<?> resultType, KeyGenerator keyGenerator,
                                                 String keyProperty, String keyColumn) {
        String statementName = mapperClass.getName() + "." + id;
        if (this.hasMappedStatement(statementName)) {
            return null;
        }
        boolean isSelect = false;
        if (sqlCommandType == SqlCommandType.SELECT) {
            isSelect = true;
        }

        return builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType,
                null, null, null, parameterType, resultMap, resultType,
                null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn,
                configuration.getDatabaseId(), languageDriver, null);
    }


    /**
     * 是否已经存在MappedStatement
     *
     * @param mappedStatement MappedStatement
     * @return true or false
     */
    private boolean hasMappedStatement(String mappedStatement) {
        return configuration.hasStatement(mappedStatement, false);
    }

}