package io.framework.myBatis.config;

import io.framework.myBatis.QuizConfiguration;
import io.framework.myBatis.config.idConfig.IdGenerator;
import io.framework.myBatis.injector.DefaultSqlInjector;
import io.framework.myBatis.injector.SqlInjector;
import lombok.Data;
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
     * 设置sql注入器
     */
    private SqlInjector sqlInjector = new DefaultSqlInjector();

    /**
     * 是否自动建表
     */
    //TODO:暂时无作用
    private Boolean buildTable = false;

    /**
     * 属性是否驼峰
     */
    //TODO:暂时无作用
    private Boolean columnHump = false;

    /**
     * 表名称是否驼峰
     */
    //TODO:暂时无作用
    private Boolean tableHump = false;

    /**
     * 插入数据返回主键,默认空实现
     */
    private KeyGenerator keyGenerator = new NoKeyGenerator();

    /**
     * 类路径
     */
    //TODO:暂时无作用
    private String classEntity;

    /**
     * 配置数据,默认quiz配置
     */
    private Configuration configuration = new QuizConfiguration();
}
