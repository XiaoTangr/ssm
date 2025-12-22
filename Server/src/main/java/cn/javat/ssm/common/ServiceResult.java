package cn.javat.ssm.common;

import lombok.Data;

@Data
public class ServiceResult<T> {
    private T data;
    private int code;
    private boolean isSuccess;

    private ServiceResult() {
    }

    /**
     * 设置成功结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return ServiceResult code=0 isSuccess=true
     */
    public static <T> ServiceResult<T> ok(T data) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setCode(0);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * 设置失败结果
     *
     * @param code 错误码
     * @param <T>  数据类型
     * @return ServiceResult code=code isSuccess=false
     */
    public static <T> ServiceResult<T> error(int code) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setCode(code);
        result.setSuccess(false);
        return result;
    }
}