package io.framework.myBatis.exception;

import lombok.Getter;

/**
 * MyBatisException 异常处理
 * Description:  
 * date: 2020/10/22 20:58 
 *
 * @author zuo  
 * @since JDK 1.8
 */
@Getter
public class QuizMyBatisException extends RuntimeException {

    private final int code;
    private final String snapshot;

    public QuizMyBatisException(String message) {
        super(message);
        this.code = 0;
        this.snapshot = "无快照";
    }

}