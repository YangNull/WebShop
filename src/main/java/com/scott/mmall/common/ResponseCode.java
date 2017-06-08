package com.scott.mmall.common;

/**
 * Created by Ubuntu on 2017/5/24.
 */
public enum ResponseCode {
    //成功
    SUCCESS(0, BaseConstant.MSG_SUCCESS),
    //错误
    ERROR(1, BaseConstant.MSG_ERROR),
    //需要登陆
    NEED_LOGIN(10, BaseConstant.MSG_NEED_LOGIN),
    //非法参数
    ILLEGAL_ARGUMENT(2, BaseConstant.MSG_PARAMETER_ERROR),
    //没有权限操作
    NO_PERMISSION(-1, BaseConstant.MSG_NO_PERMISSION);

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
