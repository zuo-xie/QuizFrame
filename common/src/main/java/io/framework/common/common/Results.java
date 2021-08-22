package io.framework.common.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Results<T> implements Serializable {

    private Integer retCode = 0;

    private String retMsg;

    private Boolean success;

    private T data;

    public static <T> Results<T> buildSuccess(Integer code, String message, T data) {
        return new Results<T>(code, message, true, data);
    }

    public static <T> Results<T> buildSuccess(Integer code, String message) {
        Results<T> Results = new Results<T>();
        Results.setRetCode(code);
        Results.setRetMsg(message);
        Results.setSuccess(true);
        return Results;
    }

    public static <T> Results<T> buildSuccess(String message) {
        Results<T> Results = new Results<T>();
        Results.setRetCode(0);
        Results.setRetMsg(message);
        Results.setSuccess(true);
        return Results;
    }

    public static <T> Results<T> buildSuccess(String message, T data) {
        Results<T> Results = new Results<T>();
        Results.setRetMsg(message);
        Results.setSuccess(true);
        Results.setData(data);
        return Results;
    }

    public static <T> Results<T> buildFailed(Integer code, String message) {
        Results<T> Results = new Results<T>();
        Results.setRetCode(code);
        Results.setRetMsg(message);
        Results.setSuccess(false);
        return Results;
    }

    public static <T> Results<T> buildFailed(Integer code, String message, T data) {
        Results<T> Results = new Results<T>();
        Results.setRetCode(code);
        Results.setRetMsg(message);
        Results.setSuccess(false);
        Results.setData(data);
        return Results;
    }

    public static <T> Results<T> buildFailed(String message) {
        Results<T> Results = new Results<T>();
        Results.setRetCode(0);
        Results.setRetMsg(message);
        Results.setSuccess(false);
        return Results;
    }


}
