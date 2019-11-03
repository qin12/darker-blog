package com.darker.blog.common;

import lombok.*;

import java.io.Serializable;

@Builder
@ToString
@AllArgsConstructor
public class Result<T> implements Serializable {

    @Getter
    @Setter
    private int code = CommonConstant.SUCCESS;

    @Getter
    @Setter
    private Object msg = "success";

    @Getter
    @Setter
    private T data;

    public Result() {
        super();
    }
    @SuppressWarnings("unchecked")
    public Result(T data) {
        super();
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public Result(CommonEnum enums) {
        super();
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }

    public Result(Throwable e) {
        super();
        this.code = CommonConstant.ERROR;
        this.msg = e.getMessage();
    }

    public Result(String message, Throwable e) {
        super();
        this.msg = message;
        this.code = CommonConstant.ERROR;
    }
}
