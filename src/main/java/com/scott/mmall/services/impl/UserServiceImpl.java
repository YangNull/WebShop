package com.scott.mmall.services.impl;

import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.common.TokenCache;
import com.scott.mmall.dao.UserMapper;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.IUserService;
import com.scott.mmall.util.GeneratorUtil;
import com.scott.mmall.util.MD5Util;
import com.scott.mmall.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ubuntu on 2017/5/24.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int rowCount = userMapper.checkUserName(username);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_DOES_NOT_EXIST);
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USERNAME_OR_PASSWORD_ERROR);
        }
        //将密码设置为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(BaseConstant.MSG_LOGIN_SUCCESS, user);
    }

    /**
     * 用户注册
     *
     * @param userVo
     * @return
     */
    @Override
    public ServerResponse<String> register(UserVo userVo) {
        //判断用户是否已经存在
        ServerResponse response = checkValid(userVo.getUsername(), BaseConstant.TYPE_USERNAME);
        if (response.isSuccess()) {
            //用户已经存在
            return response;
        }
        //判断邮箱是否是已经存在
        response = checkValid(userVo.getEmail(), BaseConstant.TYPE_EMAIL);
        if (response.isSuccess()) {
            //邮箱已经存在
            return response;
        }
        User user = new User();
        user.setUsername(userVo.getUsername());
        user.setPassword(MD5Util.MD5EncodeUtf8(userVo.getPassword()));
        user.setEmail(userVo.getEmail());
        user.setPhone(userVo.getPhone());
        user.setQuestion(userVo.getQuestion());
        user.setAnswer(userVo.getAnswer());
        user.setRole(BaseConstant.Role.ROLE_CUSTOMER);
        int result = userMapper.insert(user);
        if (result == 0) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_REGISTER_FAILUER);
        }
        return ServerResponse.createBySuccessMessage(BaseConstant.MSG_REGISTER_SUCCESS);
    }

    /**
     * 判断数据库是否存在这一条记录,如果存在则返回成功
     *
     * @param value 值
     * @param type  类型，数据库里面的字段
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        int result;
        if (BaseConstant.TYPE_EMAIL.equals(type)) {
            result = userMapper.checkUserEmail(value);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage(BaseConstant.MSG_USER_EMAIL_EXIST);
            }
        } else if (BaseConstant.TYPE_USERNAME.equals(type)) {
            result = userMapper.checkUserName(value);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage(BaseConstant.MSG_USER_EXIST);
            }
        }
        return ServerResponse.createByError();
    }

    /**
     * 查询该用户的问题，用于没登陆状态的时候修改密码功能
     *
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> queryQuestion(String username) {
        ServerResponse validResponse = checkValid(username, BaseConstant.TYPE_USERNAME);
        if (!validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_DOES_NOT_EXIST);
        }
        String question = userMapper.selectQuestionByUserName(username);
        if (StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_QUESTION_DOES_NOT_EXIST);
        }
        return ServerResponse.createBySuccess(question);
    }

    /**
     * 检查问题的答案,用于没登陆状态的时候修改密码功能
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        ServerResponse validResponse = checkValid(username, BaseConstant.TYPE_USERNAME);
        if (!validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_DOES_NOT_EXIST);
        }
        int result = userMapper.checkAnswer(username, question, answer);
        if (result > 0) {
            String token = GeneratorUtil.generatorToken();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage(BaseConstant.MSG_ANSWER_IS_ERROR);
    }

    /**
     * 设置新的密码，用于用户没登陆的时候忘记密码
     *
     * @param username    用户名
     * @param passwordNew 新的密码
     * @param forgetToken 回答问题产生的Token
     * @return
     */
    @Override
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        //用户不存在
        if (!checkValid(username, BaseConstant.TYPE_USERNAME).isSuccess()) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_DOES_NOT_EXIST);
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        System.out.println("token==>" + token);
        //token失效或者不存在
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_TOKEN_DOES_NOT_EXIST);
        }
        if (StringUtils.equals(token, forgetToken)) {
            int rowCount = userMapper.updatePasswordByUserName(username, MD5Util.MD5EncodeUtf8(passwordNew));
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage(BaseConstant.MSG_UPDATE_PASSWORD_SUCCESS);
            }
        } else {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_TOKEN_NOT_MATCH);
        }
        return ServerResponse.createByErrorMessage(BaseConstant.MSG_UPDATE_PASSWORD_ERROR);
    }

    /**
     * 重置密码，用于登陆的用户修改自己的密码
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public ServerResponse<String> restPassword(User user, String oldPassword, String newPassword) {
        if (userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(oldPassword)) > 0) {
            user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                return ServerResponse.createBySuccessMessage(BaseConstant.MSG_UPDATE_PASSWORD_SUCCESS);
            }
        } else {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_UPDATE_PASSWORD_ERROR);
        }
        return ServerResponse.createByErrorMessage(BaseConstant.MSG_UPDATE_PASSWORD_ERROR);
    }

    @Override
    public ServerResponse<User> updateUserInformation(User user) {
        if (userMapper.checkUserEmailByUsername(user.getEmail(), user.getUsername()) > 0) {
            //邮箱已经有其它用户注册过了
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_EMAIL_EXIST);
        }
        //用于更新，只允许这几个字段
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        System.out.println("updateUser : " + ToStringBuilder.reflectionToString(updateUser, ToStringStyle.JSON_STYLE));
        if (userMapper.updateByPrimaryKeySelective(updateUser) > 0) {
            return ServerResponse.createBySuccess(BaseConstant.MSG_UPDATE_USER_INFORMATION_SUCCESS, updateUser);
        }
        return ServerResponse.createByErrorMessage(BaseConstant.MSG_UPDATE_USER_INFORMATION_ERROR);
    }

    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public ServerResponse<User> queryUserInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage(BaseConstant.MSG_USER_DOES_NOT_EXIST);
    }

    @Override
    public ServerResponse checkAdmin(User user) {
        if (user != null || user.getRole() == BaseConstant.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}

