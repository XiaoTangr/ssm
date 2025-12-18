package cn.javat.ssm.common;

import lombok.*;

/**
 * 封装接口响应体的类，支持链式调用,必须先设置code，然后才能设置msg和data
 */
public class ResponsePayload {
    // 响应状态（比如 200 表示成功，500 表示失败）
    private int code;
    // 提示信息
    private String msg;
    // 响应数据（泛型支持任意类型）
    private Object data;

    // 私有构造方法，避免外部直接new，强制通过静态方法创建
    private ResponsePayload() {
    }

    public static ResponsePayload builder() {
        return new ResponsePayload();
    }

    public ResponsePayload ok() {
        this.code = 200;
        return this;
    }

    public ResponsePayload bad() {
        this.code = 400;
        return this;
    }

    public ResponsePayload innerError() {
        this.code = 500;
        return this;
    }

    public ResponsePayload unauthorized() {
        this.code = 401;
        return this;
    }

    public ResponsePayload notFound() {
        this.code = 404;
        return this;
    }

    public ResponsePayload msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponsePayload data(Object data) {
        this.data = data;
        return this;
    }

    public ResponsePayload code(int code) {
        this.code = code;
        return this;
    }

}