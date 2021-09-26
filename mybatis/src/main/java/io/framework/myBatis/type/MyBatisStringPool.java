package io.framework.myBatis.type;

import lombok.Getter;

@Getter
public enum MyBatisStringPool {

    LEFT_BRACE("{", "左花括号"),
    RIGHT_BRACE("}", "右花括号"),
    E_COMMA(",","英文逗号"),
    LEFT_ANGLE_BRACKETS("<","左尖括号"),
    RIGHT_ANGLE_BRACKETS(">","右尖括号"),
    E_LEFT_BRACKETS("(","英文左括号"),
    E_RIGHT_BRACKETS(")","英文右括号"),

    SPACE(" ","空格"),
    AND(" and ","和"),
    SINGLE_QUOTE("\"","单引号"),
    WRAP("\n","换行"),
    SLASH("/","斜杠"),
    TEXT("text","MyBatis text属性"),
    TEST("test","MyBatis test属性"),
    EQUAL("=","等号"),
    NUMBER_SIGN("#","井号"),
    USD("$","美元符"),
    TRIM("trim","Mybatis  trim标签"),
    SUFFIX("suffix","Mybatis  suffix属性"),
    SUFFIX_OVERRIDES("suffixOverrides","MyBatis suffixOverrides属性"),
    PREFIX_OVERRIDES("prefixOverrides","MyBatis prefixOverrides属性"),
    PREFIX("prefix","Mybatis  prefix属性"),
    IF("if","MyBatis  if标签"),
    WHERE("where","MyBatis where 标签"),
    NOT_NULL("!= null","不为空"),
    VALUES("values","mysql 预留字段"),
    SCRIPT("script","mybatis字段 script"),
    POINT(".","点"),
    SQL_WRAPPER("sqlWrapper","自定义填充类型"),
    APOSTROPHE("'","单引号")
    ;

    private final String type;
    private final String data;

    MyBatisStringPool(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
