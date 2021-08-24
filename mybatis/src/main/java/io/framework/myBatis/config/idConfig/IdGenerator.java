package io.framework.myBatis.config.idConfig;

/**
 * Id生成器
 * Description:  
 * date: 2020/12/27 11:29 
 *
 * @author zuo  
 * @since JDK 1.8
 */
public interface IdGenerator {

    /**
     * 生成一个主键Id（Long）
     * @return 主键Id
     */
    long lCreatorId();

    /**
     * 生成一个主键Id（String）
     * @return
     */
    String sCreatorId();
}