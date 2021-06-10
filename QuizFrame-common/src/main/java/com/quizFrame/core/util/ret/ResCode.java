package com.quizFrame.core.util.ret;

import lombok.Getter;

@Getter
public enum ResCode {
    SUCCESS(200, "成功",true),
    ERROR(400, "失败",false);

    private final Integer code;
    private final String msg;
    private final Boolean status;

    ResCode(Integer code, String msg,Boolean status) {
        this.code = code;
        this.msg = msg;
        this.status = status;
    }
}
