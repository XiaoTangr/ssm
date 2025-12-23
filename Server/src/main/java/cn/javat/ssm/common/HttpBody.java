package cn.javat.ssm.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

/**
 * 封装接口响应体的类，支持链式调用
 */
@Data
public class HttpBody {
    // 响应状态（比如 200 表示成功，500 表示失败）
    private int code;
    // 提示信息
    private String msg;
    // 响应数据（支持任意类型）
    private Object data;

    // 私有构造方法，避免外部直接new，强制通过静态方法创建
    private HttpBody() {
    }


    public static HttpBody ok() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(200);
        return httpBody;
    }

    public static HttpBody bad() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(400);
        return httpBody;
    }

    public static HttpBody innerError() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(500);
        return httpBody;
    }

    public static HttpBody unauthorized() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(401);
        return httpBody;
    }

    public static HttpBody forbidden() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(403);
        return httpBody;
    }

    public static HttpBody notFound() {
        HttpBody httpBody = new HttpBody();
        httpBody.setCode(404);
        return httpBody;
    }

    public HttpBody msg(String msg) {
        this.msg = msg;
        return this;
    }

    public <T> HttpBody data(T data) {
        this.data = data;
        return this;
    }

    public HttpBody code(int code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
//        使用Jackson序列化为JSON，没有JsonUtil
        ObjectMapper objectMapper = new ObjectMapper();
         try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }
}