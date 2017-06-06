package com.scott.mmall.vo;

import com.scott.mmall.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Ubuntu on 2017/5/25.
 */
public class UserVo {
    private String username;
    private String password;
    private String newPassword;
    private String email;
    private String phone;
    private String question;
    private String answer;

    public UserVo() {

    }

    public UserVo(String username, String password, String newPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public UserVo(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }

    public UserVo(String username, String password, String email, String phone, String question, String answer) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
    }

    public UserVo(String username, String password, String newPassword, String email, String phone, String question, String answer) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 判断前端传过来的参数是否有参数为空值
     *
     * @return
     */
    public boolean parameterIsBlank() {
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(email) ||
                StringUtils.isBlank(phone) ||
                StringUtils.isBlank(question) ||
                StringUtils.isBlank(answer)) {
            return true;
        }
        return false;
    }
}
