package cn.javat.ssm.common;

/**
 * 统一API响应结果封装类
 */
public class ApiResponse {
    private int code;
    private String message;
    private Object data;
    
    public ApiResponse() {
    }
    
    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应，只返回数据
     * @param data 响应数据
     * @return ApiResponse
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "success", data);
    }
    
    /**
     * 成功响应，返回自定义消息和数据
     * @param message 响应消息
     * @param data 响应数据
     * @return ApiResponse
     */
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(200, message, data);
    }
    
    /**
     * 成功响应，只返回消息
     * @param message 响应消息
     * @return ApiResponse
     */
    public static ApiResponse success(String message) {
        return new ApiResponse(200, message, null);
    }
    
    /**
     * 错误响应，返回错误码和消息
     * @param code 错误码
     * @param message 错误消息
     * @return ApiResponse
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, message, null);
    }
    
    /**
     * 错误响应，默认500错误码
     * @param message 错误消息
     * @return ApiResponse
     */
    public static ApiResponse error(String message) {
        return new ApiResponse(500, message, null);
    }
    
    /**
     * 401未授权响应
     * @param message 错误消息
     * @return ApiResponse
     */
    public static ApiResponse unauthorized(String message) {
        return new ApiResponse(401, message, null);
    }
    
    /**
     * 403禁止访问响应
     * @param message 错误消息
     * @return ApiResponse
     */
    public static ApiResponse forbidden(String message) {
        return new ApiResponse(403, message, null);
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}