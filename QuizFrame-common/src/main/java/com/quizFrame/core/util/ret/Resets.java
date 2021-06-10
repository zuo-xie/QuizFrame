package com.quizFrame.core.util.ret;

import lombok.Data;

/**
 * 工具类返回类
 */
@Data
public class Resets<T> {

    private T data;

    private Integer code;

    private String msg;

    private Boolean status;

    private Resets() {

    }

    public static <T> Resets<T> success(Integer code, T data, String msg) {
        Resets<T> resets = new Resets<T>();
        resets.setStatus(true);
        resets.setCode(code);
        resets.setData(data);
        resets.setMsg(msg);
        return resets;
    }

    public static <T> Resets<T> success(T data) {
        Resets<T> resets = new Resets<T>();
        resets.setStatus(ResCode.SUCCESS.getStatus());
        resets.setCode(ResCode.SUCCESS.getCode());
        resets.setData(data);
        resets.setMsg(ResCode.SUCCESS.getMsg());
        return resets;
    }

    public static <T> Resets<T> error(T data) {
        Resets<T> resets = new Resets<T>();
        resets.setStatus(ResCode.ERROR.getStatus());
        resets.setCode(ResCode.ERROR.getCode());
        resets.setData(data);
        resets.setMsg(ResCode.ERROR.getMsg());
        return resets;
    }

}
