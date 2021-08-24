package io.framework.myBatis.comment;


import lombok.Data;
import mybatis.frame.comment.enums.MySqlDML;
import mybatis.frame.exception.QuizMyBatisException;
import mybatis.frame.function.CustomFunOne;
import mybatis.frame.function.CustomFunTwo;
import mybatis.frame.util.MyBatisStringPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表结构存储类
 *
 * @author zuo
 */
@Data
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 描述
     */
    private String comment;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 字段值
     */
    private List<ColumnInfo> column;

    /**
     * 主键对应字段
     */
    private ColumnInfo perKey;

    /**
     * 实体类型
     */
    private Class<?> entityType;

    /**
     * resMap集合
     */
    private List<ResultMapping> resultMappingList;
    /**
     * resMap id
     */
    private String resultMapName;

    /**
     * 获取所有的字段 <br>
     * 例如：a,b
     *
     * @return 该类下的所有字段，并将其分隔完成
     */
    public String getAllSqlSelect() throws Exception {
        if (!(this.column == null || this.column.isEmpty())) {
            List<String> columnInfos = column.stream().map(ColumnInfo::getColumnName).collect(Collectors.toList());
            return StringUtils.join(columnInfos, MyBatisStringPool.E_COMMA.getType());
        } else {
            throw new QuizMyBatisException(entityType.getName() + "类没有对应数据字段");
        }
    }

    /**
     * 整合成mybatis  trim标签语句
     *
     * @param prefix          给sql语句拼接的前缀
     * @param suffix          给sql语句拼接的后缀
     * @param prefixOverrides 去除sql语句前面的关键字或者字符
     * @param suffixOverrides 去除sql语句后面的关键字或者字符
     * @return 拼接后的语句
     */
    public String convertTrim(String prefix,
                              String suffix,
                              String prefixOverrides,
                              String suffixOverrides,
                              MySqlDML mySqlDML,
                              CustomFunTwo<StringBuilder> fun) {
        if (!(this.column == null || this.column.isEmpty())) {
            StringBuilder sb = new StringBuilder(
                    MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                    .append(MyBatisStringPool.TRIM.getType());

            if (StringUtils.isNotBlank(prefix)) {
                sb.append(this.addAttributes(MyBatisStringPool.PREFIX.getType(), prefix));
            }
            if (StringUtils.isNotBlank(suffix)) {
                sb.append(this.addAttributes(MyBatisStringPool.SUFFIX.getType(), suffix));
            }
            if (StringUtils.isNotBlank(prefixOverrides)) {
                sb.append(this.addAttributes(MyBatisStringPool.PREFIX_OVERRIDES.getType(), prefix));
            }
            if (StringUtils.isNotBlank(suffixOverrides)) {
                sb.append(this.addAttributes(MyBatisStringPool.SUFFIX_OVERRIDES.getType(), suffixOverrides));
            }
            sb.append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType());
            sb.append(MyBatisStringPool.WRAP.getType());

            sb = fun.method(sb);

            sb.append(MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                    .append(MyBatisStringPool.SLASH.getType())
                    .append(MyBatisStringPool.TRIM.getType())
                    .append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType());

            return sb.toString();
        } else {
            throw new QuizMyBatisException(entityType.getName() + "类没有对应数据字段");
        }
    }

    /**
     * 整合成MyBatis  if标签语句
     *
     * @param value 参数
     * @return 拼接后的结果
     */
    public StringBuilder convertIf(String value, CustomFunOne<StringBuilder> fun) {
        return this.buildLabel(MyBatisStringPool.IF.getType(),
                this.addAttributes(MyBatisStringPool.TEST.getType(), value),
                fun);
    }

    public StringBuilder convertWhere(CustomFunOne<StringBuilder> fun) {
        return this.buildLabel(MyBatisStringPool.WHERE.getType(),
                new StringBuilder(),
                fun);
    }

    public StringBuilder notNull(String value) {
        return null;
    }

    /**
     * 整合成MyBatis  choose标签语句
     *
     * @param value 参数
     * @return 拼接后的结果
     */
    public StringBuilder convertChoose(String value, CustomFunOne<StringBuilder> fun) {
        return null;
    }


    /***
     * 添加属性
     * @param key 属性对象
     * @param value 对应值
     * @return key = "value"
     */
    private StringBuilder addAttributes(String key, String value) {
        return new StringBuilder(MyBatisStringPool.SPACE.getType())
                .append(key)
                .append(MyBatisStringPool.EQUAL.getType())
                .append(MyBatisStringPool.SINGLE_QUOTE.getType())
                .append(value)
                .append(MyBatisStringPool.SINGLE_QUOTE.getType())
                .append(MyBatisStringPool.SPACE.getType());
    }

    /**
     * 生成标签
     *
     * @param labelName  标签名称
     * @param attributes 标签属性
     * @param fun        内部语句
     * @return 例子： <a></a>
     */
    private StringBuilder buildLabel(String labelName, StringBuilder attributes, CustomFunOne<StringBuilder> fun) {
        return new StringBuilder()
                .append(MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                .append(labelName)
                .append(MyBatisStringPool.SPACE.getType())
                .append(attributes)
                .append(MyBatisStringPool.SPACE.getType())
                .append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.WRAP.getType())
                .append(fun.method())
                .append(MyBatisStringPool.WRAP.getType())
                .append(MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.SLASH.getType())
                .append(labelName)
                .append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.WRAP.getType());
    }

    public StringBuilder builderScript(String value) {
        return new StringBuilder()
                .append(MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.SCRIPT.getType())
                .append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.WRAP.getType())
                .append(value)
                .append(MyBatisStringPool.WRAP.getType())
                .append(MyBatisStringPool.LEFT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.SLASH.getType())
                .append(MyBatisStringPool.SCRIPT.getType())
                .append(MyBatisStringPool.RIGHT_ANGLE_BRACKETS.getType())
                .append(MyBatisStringPool.WRAP.getType());
    }
}
