package com.scott.mmall.controller.portal;

import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.IUserService;
import com.scott.mmall.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Ubuntu on 2017/5/24.
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆接口
     *
     * @param username 用户名
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestParam(required = true) String username, @RequestParam(required = true) String password, HttpSession session) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(BaseConstant.KEY_CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户登出接口
     *
     * @param session
     * @return
     */

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> logout(HttpSession session) {
        session.removeAttribute(BaseConstant.KEY_CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册接口
     *
     * @param user vo用户信息对象
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(UserVo user) {
        //user不为空并且参数不为空
        //TODO 检查参数是否为合格参数，如密码强度，用户名要求...
        if (user == null || user.parameterIsBlank()) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String value, String type) {
        if (!StringUtils.isNotBlank(type) && !StringUtils.isNotBlank(type)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iUserService.checkValid(value, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("MSG_NEED_LOGIN");

    }

    /**
     * 不登陆状态下忘记密码，用户名查找问题回答正确修改密码
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(@RequestParam(required = true) String username) {
        //TODO 数据验证，用户验证
        //TODO 向注册的邮箱发送验证链接
        if (StringUtils.isBlank(username)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iUserService.queryQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码，没登陆状态
     *
     * @param username    用户名
     * @param passwordNew 新的密码
     * @param forgetToken token
     * @return
     */
    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken) || StringUtils.isBlank(username) || StringUtils.isBlank(passwordNew)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }

    /**
     * 修改密码，用于登陆 的用户修改密码
     *
     * @param session
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpSession session, String oldPassword, String newPassword) {
        //TODO 用邮箱验+token方式去重置密码
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_NEED_LOGIN);
        }
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iUserService.restPassword(user, oldPassword, newPassword);
    }

    @RequestMapping(value = "update_user_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInformation(HttpSession session, User user) {
        //TODO 接收数据不能用POJO的User，用VoUser，把敏感字段禁止更新，id,username,password
        User currentUser = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_NEED_LOGIN);
        }
        //更新用户信息，id不能更改，还有用户名不能更改
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(BaseConstant.KEY_CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 登陆用户查询用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_NEED_LOGIN);
        }
        return iUserService.queryUserInformation(currentUser.getId());
    }

    @RequestMapping(value = "test.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> test(String username, String password, int type) {
        if (type == 1) {
            return ServerResponse.createBySuccess("Test Success", username + "[]" + password);
        } else {
            return ServerResponse.createByErrorMessage("Test Error");
        }
    }
}
