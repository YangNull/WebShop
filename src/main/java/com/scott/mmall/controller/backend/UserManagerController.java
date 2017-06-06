package com.scott.mmall.controller.backend;

import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Ubuntu on 2017/5/25.
 */
@Controller
@RequestMapping(value = "/manager/user")
public class UserManagerController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(HttpSession session, String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            if (response.getData().getRole() == BaseConstant.Role.ROLE_ADMIN) {
                session.setAttribute(BaseConstant.KEY_CURRENT_USER, response.getData());
                return ServerResponse.createBySuccess(BaseConstant.MSG_LOGIN_SUCCESS, response.getData());
            } else {
                return ServerResponse.createByErrorMessage("you not is admin");
            }
        }
        return ServerResponse.createByErrorMessage("login error");
    }
}
