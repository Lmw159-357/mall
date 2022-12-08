package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

/**
 * 描述：   API统一（通用）返回对象；
 */
public class ApiRestResponse<T> {
    private Integer status;//状态码
    private String msg;//描述信息
    private T data;//接口返回的数据

    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }

    /**
     * 处理成功，但接口没有返回数据（即，data是null）；
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    /**
     * 处理成功，而且接口返回了数据（即，data是有数据的）；
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    /**
     * 处理失败；（接口处理失败了，自然data为空，即没有返回数据的）
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<>(code, msg);
    }

    /**
     * 处理失败；（接口处理失败了，自然data为空，即没有返回数据的）；同时，这儿使用了异常枚举类
     * @param ex
     * @param <T>
     * @return
     */
    public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum ex) {
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMeg() {
        return OK_MSG;
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}