package cn.javat.ssm.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ResponseSender {
    /**
     * 构建响应结果
     *
     * @param responseHeader 响应Body
     * @param responseBody   响应Body
     * @param statusCode     响应状态码
     * @return ResponseEntity
     */
    public static ResponseEntity<ResponseBody> buildResponse(HashMap<String, Object> responseHeader, ResponseBody responseBody, HttpStatus statusCode) {
    }

}