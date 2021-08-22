package io.framework.common.exception;

import io.framework.common.common.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class BusinessException {
    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Results<?> exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return Results.buildFailed("操作错误");
    }
}
