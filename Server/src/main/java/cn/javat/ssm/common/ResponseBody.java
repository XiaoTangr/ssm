package cn.javat.ssm.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBody {
    private String message;
    private Object data;
    private int code;

    private static ResponseBody buildResponseBody(String message, Object data, int code) {
        return new ResponseBody(message, data, code);
    }


    public static ResponseBody ok(Object data) {
        return buildResponseBody("操作成功", data, 200);
    }

    public static ResponseBody ok(String message, Object data) {
        return buildResponseBody(message, data, 200);
    }

    public static ResponseBody error(String message) {
        return buildResponseBody(message, null, 500);
    }

    public static ResponseBody error(String message, Object data) {
        return buildResponseBody(message, data, 500);
    }

    public static ResponseBody unauthorized(String message) {
        return buildResponseBody(message, null, 401);
    }


}
