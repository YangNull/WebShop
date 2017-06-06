package com.scott.mmall.common;

/**
 * Created by Ubuntu on 2017/5/24.
 * 基础常量，用于放置常量
 */
public class BaseConstant {
    //User Module
    public static final String MSG_USER_DOES_NOT_EXIST = "用户不存在";
    public static final String MSG_USERNAME_OR_PASSWORD_ERROR = "用户名或密码错误";
    public static final String MSG_LOGIN_SUCCESS = "登陆成功";
    public static final String MSG_USER_NOT_LOGIN = "用户没登陆";
    public static final String MSG_USER_EXIST = "该用户已经存在";
    public static final String MSG_USER_EMAIL_EXIST = "该邮箱已经存在";
    public static final String MSG_REGISTER_FAILUER = "注册失败";
    public static final String MSG_REGISTER_SUCCESS = "注册成功";
    public static final String MSG_QUESTION_DOES_NOT_EXIST = "问题不存在";
    public static final String MSG_ANSWER_IS_ERROR = "问题和答案不匹配";
    public static final String MSG_TOKEN_DOES_NOT_EXIST = "token失效或者不存在";
    public static final String MSG_TOKEN_NOT_MATCH = "token不匹配";
    public static final String MSG_UPDATE_PASSWORD_ERROR = "更新密码失败";
    public static final String MSG_UPDATE_PASSWORD_SUCCESS = "更新密码成功";
    public static final String MSG_UPDATE_USER_INFORMATION_SUCCESS = "用户信息更新成功";
    public static final String MSG_UPDATE_USER_INFORMATION_ERROR = "用户信息更新失败";
    public static final String TYPE_USERNAME = "username";
    public static final String TYPE_EMAIL = "email";
    //public
    public static final String MSG_PARAMETER_ERROR = "请求参数错误";
    public static final String KEY_CURRENT_USER = "currentUser";
    public static final String MSG_NO_PERMISSION = "没有权限操作";

    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

}
