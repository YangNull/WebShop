package com.scott.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 封装响应的复用类
 * 构造方法私有化，通过public方法获得实例
 * <p>
 * Created by Ubuntu on 2017/5/24.
 */
//如果不加上空的判断，存在NULL的数据会以一个空结点Json数据返回前端
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    private int status;//状态码
    private String msg;//消息
    private T data;//响应数据

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;

    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 这个类要序列化成一个Json数据返回给前端的，这个isSuccess就不必要返回了
     *
     * @return
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new <T>ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String successMessage) {
        return new <T>ServerResponse<T>(ResponseCode.SUCCESS.getCode(), successMessage);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new <T>ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new <T>ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByError() {
        return new <T>ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new <T>ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCode(int errorCode, String errorMessage) {
        return new <T>ServerResponse<T>(errorCode, errorMessage);
    }

    public static <T> ServerResponse<T> createByResponseCode(ResponseCode responseCode) {
        return new <T>ServerResponse<T>(responseCode.getCode(), responseCode.getDesc());
    }
}
