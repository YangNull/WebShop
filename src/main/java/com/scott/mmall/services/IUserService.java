package com.scott.mmall.services;

import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.User;
import com.scott.mmall.vo.UserVo;

/**
 * Created by Ubuntu on 2017/5/24.
 */
public interface IUserService {
    public ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(UserVo userVo);

    ServerResponse<String> checkValid(String value, String type);

    ServerResponse<String> queryQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);


    ServerResponse<String> restPassword(User user, String oldPassword, String newPassword);

    ServerResponse<User> updateUserInformation(User user);

    ServerResponse<User> queryUserInformation(Integer id);

    ServerResponse checkAdmin(User user);
}
