package io.framework.myBatis.config;

import lombok.Data;
import mybatis.frame.QuizConfiguration;
import mybatis.frame.config.idConfig.IdGenerator;
import mybatis.frame.injector.DefaultSqlInjector;
import mybatis.frame.injector.SqlInjector;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.session.Configuration;


/**
 * 配置管理工具
 * @author zuo
 */
@Data
public class ConfigManage {

    private static ConfigManage configManage;

    private ConfigManage() {}

    public static ConfigManage getInstance() {
        if ( configManage == null) {
            synchronized (ConfigManage.class) {
                if (configManage == null) {
                    configManage = new ConfigManage();
                }
            }
        }
        return configManage;
    }
    /**
     * 主键生成器
     */
    private IdGenerator idGenerator;

    /**
     * sql注入器
     */
    private SqlInjector sqlInjector = new DefaultSqlInjector();

    /**
     * 是否自动建表
     */
    private Boolean buildTable = false;

    /**
     * 属性是否驼峰
     */
    private Boolean columnHump = false;

    /**
     * 表名称是否驼峰
     */
    private Boolean tableHump = false;

    /**
     * 对主键的默认实现
     */
    private KeyGenerator keyGenerator = new NoKeyGenerator();

    /**
     *  类路径
     */
    private String classEntity;

    /**
     * 配置数据
     */
    private Configuration configuration = new QuizConfiguration();
}
