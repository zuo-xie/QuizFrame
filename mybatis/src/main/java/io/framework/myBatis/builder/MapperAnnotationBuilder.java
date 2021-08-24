package io.framework.myBatis.builder;


import io.framework.myBatis.MapperTop;
import io.framework.myBatis.annotations.Column;
import io.framework.myBatis.annotations.PrimaryKey;
import io.framework.myBatis.annotations.Table;
import io.framework.myBatis.comment.ColumnInfo;
import io.framework.myBatis.comment.TableInfo;
import io.framework.myBatis.config.ConfigManage;
import io.framework.myBatis.exception.QuizMyBatisException;
import io.framework.myBatis.injector.SqlInjector;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.session.Configuration;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MapperAnnotationBuilder extends org.apache.ibatis.builder.annotation.MapperAnnotationBuilder {

    /**
     * SQL助理类型
     */
    private static final Set<Class<? extends Annotation>> statementAnnotationTypes = Stream
            .of(Select.class, Update.class, Insert.class, Delete.class, SelectProvider.class, UpdateProvider.class,
                    InsertProvider.class, DeleteProvider.class)
            .collect(Collectors.toSet());

    /**
     * 映射器构建助理类
     */
    private final MapperBuilderAssistant myAssistant;

    /**
     * MyBatis配置类
     */
    private final Configuration configuration;

    /**
     * 当前接口
     */
    private final Class<?> type;

    private final SqlInjector sqlInjector = ConfigManage.getInstance().getSqlInjector();

    public MapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        super(configuration, type);
        //需要转化为指定的类型
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        this.myAssistant = new MapperBuilderAssistant(configuration, resource);
        this.type = type;
        this.configuration = configuration;
    }

    public MapperBuilderAssistant getAssistant() {
        return this.myAssistant;
    }

    /**
     * 原本是用来解析mapper接口对应的mapper标签下的所有方法和标签，并封装起来<br>
     * 现在对其修改，保留原来的功能再添加默认的CRUD接口
     */
    @Override
    public void parse() {
        String resource = type.toString();
        //判断资源是否以加载，已加载不进行操作
        if (!configuration.isResourceLoaded(resource)) {
            loadXmlResource();
            //加载资源
            configuration.addLoadedResource(resource);
            String mapperName = type.getName();
            //设置助理类，命名空间
            myAssistant.setCurrentNamespace(mapperName);
            //解析CacheNamespace注解 -，构建一个Cache对象，并保存到Mybatis全局配置信息中
            parseCache();
            //解析CacheNamespace注解，引用CacheRef对应的Cache对象
            parseCacheRef();
            //判断mapper接口是否继承 MapperTop
            Type[] genericInterfaces = type.getGenericInterfaces();

            Arrays.stream(genericInterfaces)
                    .forEach(v -> {
                        ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) v;
                        List<Class<?>> list = new ArrayList<>();
                        if (typeImpl.getRawType().equals(MapperTop.class)) {
                            try {
                                Type[] typeList = typeImpl.getActualTypeArguments();
                                List<Type> types = Arrays.asList(typeList);

                                for (Type type1 : types) {
                                    list.add(Class.forName(type1.getTypeName()));
                                }

                                Class<?> aClass = (Class<?>) types.get(0);
                                //转化的泛型

                                TableInfo tableInfo = this.getTableInfo(aClass);

                                //TODO:设置resultMaps
                                ResultMap resultMap = new ResultMap.Builder(this.configuration,
                                        type.getCanonicalName() + "." + tableInfo.getResultMapName(),
                                        tableInfo.getEntityType(),
                                        tableInfo.getResultMappingList()).build();
                                this.configuration.addResultMap(resultMap);

                                //注入sql
                                sqlInjector.inspectInject(type, myAssistant, list, tableInfo);
                            } catch (Exception e) {

                            }
                        }
                    });
        }
    }

    private TableInfo getTableInfo(Class<?> aClass) throws Exception {
        //表
        TableInfo tableInfo = new TableInfo();
        //列属性
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        //获取该类的所有注解,并判断是否包含 @Table注解
        Annotation[] annotations = aClass.getAnnotations();
        boolean b = Arrays.stream(annotations).anyMatch(v -> v.annotationType().equals(Table.class));
        //如果不包含退出
        if (!b) {
            throw new QuizMyBatisException(aClass.getName() + "不存在注解 @Table");
        }
        //类名称
        //TODO ： 需要转化为 小写下滑线模式
        String className = aClass.getName();
        //获取表属性
        Arrays.stream(annotations).forEach(v -> {
            if (v.annotationType().getName().equals(Table.class.getName())) {
                //转换赋值 （得到表名和描述）
                Table table = (Table) v;
                String tableName = table.tableName();
                if (StringUtils.isBlank(tableName)) {
                    tableName = className;
                }
                tableInfo.setTableName(tableName);
                tableInfo.setComment(table.comment());
                tableInfo.setCharset(table.charset());
            }
        });
        //获取类的属性
        Field[] fieldList = aClass.getDeclaredFields();
        List<ResultMapping> resultMappingList = new ArrayList<>();
        for (Field field : fieldList) {
            ColumnInfo columnInfo = new ColumnInfo();
            boolean b1 = Arrays.stream(field.getAnnotations()).anyMatch(v -> v.annotationType().getName().equals(Column.class.getName()));
            if (!b1) {
                continue;
            }
            boolean b2 = Arrays.stream(field.getAnnotations()).anyMatch(v -> v.annotationType().getName().equals(PrimaryKey.class.getName()));
            String fieldName = field.getName();
            Arrays.stream(field.getAnnotations()).forEach(v -> {
                if (v.annotationType().getName().equals(Column.class.getName())) {
                    Column column = (Column) v;
                    String columnName = column.columnName();
                    if (StringUtils.isBlank(columnName)) {
                        columnName = fieldName;
                    }

                    ResultMapping.Builder builder =
                            new ResultMapping.Builder(this.configuration,
                                    fieldName,
                                    column.columnName(),
                                    field.getType());

                    columnInfo.setColumnName(columnName);
                    columnInfo.setIsNull(column.notNull());
                    columnInfo.setLength(column.length());
                    columnInfo.setPrimaryKey(b2);
                    columnInfo.setTypes(column.type().getType());
                    builder.jdbcType(column.type().getJdbcType());
                    columnInfo.setComment(column.comment());
                    columnInfo.setFieId(field.getName());
                    if (b2) {
                        tableInfo.setPerKey(columnInfo);
                        List<ResultFlag> resultFlags = new ArrayList<>();
                        resultFlags.add(ResultFlag.ID);
                        builder.flags(resultFlags);
                    } else {
                        builder.flags(new ArrayList<>());
                    }
                    ResultMapping build = builder.build();
                    resultMappingList.add(build);
                    columnInfoList.add(columnInfo);
                }
            });
        }
        tableInfo.setColumn(columnInfoList);
        tableInfo.setEntityType(aClass);
        tableInfo.setResultMappingList(resultMappingList);
        tableInfo.setResultMapName("BaseMapping");
        return tableInfo;
    }

    //copy过来的
    private void loadXmlResource() {
        // Spring may not know the real resource name so we check a flag
        // to prevent loading again a resource twice
        // this flag is set at XMLMapperBuilder#bindMapperForNamespace
        if (!configuration.isResourceLoaded("namespace:" + type.getName())) {
            String xmlResource = type.getName().replace('.', '/') + ".xml";
            // #1347
            InputStream inputStream = type.getResourceAsStream("/" + xmlResource);
            if (inputStream == null) {
                // Search XML mapper that is not in the module but in the classpath.
                try {
                    inputStream = Resources.getResourceAsStream(type.getClassLoader(), xmlResource);
                } catch (IOException e2) {
                    // ignore, resource is not required
                }
            }
            if (inputStream != null) {
                XMLMapperBuilder xmlParser = new XMLMapperBuilder(inputStream, myAssistant.getConfiguration(), xmlResource, configuration.getSqlFragments(), type.getName());
                xmlParser.parse();
            }
        }
    }

    /**
     * 解析CacheNamespace注解
     */
    private void parseCache() {
        CacheNamespace cacheDomain = type.getAnnotation(CacheNamespace.class);
        if (cacheDomain != null) {
            Integer size = cacheDomain.size() == 0 ? null : cacheDomain.size();
            Long flushInterval = cacheDomain.flushInterval() == 0 ? null : cacheDomain.flushInterval();
            Properties props = convertToProperties(cacheDomain.properties());
            myAssistant.useNewCache(cacheDomain.implementation(), cacheDomain.eviction(), flushInterval, size, cacheDomain.readWrite(), cacheDomain.blocking(), props);
        }
    }

    private Properties convertToProperties(Property[] properties) {
        if (properties.length == 0) {
            return null;
        }
        Properties props = new Properties();
        for (Property property : properties) {
            props.setProperty(property.name(),
                    PropertyParser.parse(property.value(), configuration.getVariables()));
        }
        return props;
    }

    private void parseCacheRef() {
        CacheNamespaceRef cacheDomainRef = type.getAnnotation(CacheNamespaceRef.class);
        if (cacheDomainRef != null) {
            Class<?> refType = cacheDomainRef.value();
            String refName = cacheDomainRef.name();
            if (refType == void.class && refName.isEmpty()) {
                throw new BuilderException("Should be specified either value() or name() attribute in the @CacheNamespaceRef");
            }
            if (refType != void.class && !refName.isEmpty()) {
                throw new BuilderException("Cannot use both value() and name() attribute in the @CacheNamespaceRef");
            }
            String namespace = (refType != void.class) ? refType.getName() : refName;
            try {
                myAssistant.useCacheRef(namespace);
            } catch (IncompleteElementException e) {
                configuration.addIncompleteCacheRef(new CacheRefResolver(myAssistant, namespace));
            }
        }
    }
}
