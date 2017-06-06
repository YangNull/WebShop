package com.scott.mmall.servicesTest;

import com.scott.mmall.base.BaseJunitTest;
import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.IUserService;
import com.scott.mmall.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Ubuntu on 2017/5/25.
 */

public class UserServiceTest extends BaseJunitTest {
    private Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    @Autowired
    private IUserService iUserService;

    @Override
    public void before() {
        super.before();
    }

    @Test
    public void test() {
        Assert.assertNotNull(iUserService);
    }

    @Test
    public void login() {
        String username = "admin";
        String password = "admin123";
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()) {
            System.out.println(response.getMsg());
            User user = (User) response.getData();
            System.out.println(ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
            // restPassword(user, "admin", "admin123");//登陆后重置密码
            //更新用户的信息
            user.setEmail("admin@admin.com");
            user.setPhone("123456789");
            updateUserInformation(user);
        } else {
            System.out.println(response.getMsg());
        }
    }

    public void restPassword(User user, String oldPassword, String newPassword) {
        if (user == null || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            System.out.println(BaseConstant.MSG_PARAMETER_ERROR);
            return;
        }
        ServerResponse response = iUserService.restPassword(user, oldPassword, newPassword);
        System.out.println(response.getStatus() + "\t" + response.getMsg());
    }

    @Test
    public void register() {
        UserVo userVo = new UserVo();
        userVo.setUsername("kang");
        userVo.setPassword("yang");
        userVo.setEmail("kang@163.com");
        userVo.setPhone("123456789");
        userVo.setQuestion("111");
        userVo.setAnswer("222");
        if (userVo == null || userVo.parameterIsBlank()) {
            System.out.println(BaseConstant.MSG_PARAMETER_ERROR);
        } else {
            ServerResponse response = iUserService.register(userVo);
            System.out.println(response.getMsg());
        }
    }

    @Test
    public void queryQuestion() {
        String username = "yang";
        ServerResponse response = iUserService.queryQuestion(username);
        System.out.println(response.getMsg() + "\t" + response.getData() + "\t" + response.getStatus());
    }

    @Test
    public void checkAnswer() {
        String username = "yang";
        String question = "111";
        String answer = "222";
        ServerResponse response = iUserService.checkAnswer(username, question, answer);
        if (response.isSuccess()) {
            String token = (String) response.getData();
            System.out.println(response.getStatus() + "\t" + token);
            forgetRestPassword(username, "000", token);
        } else {
            System.out.println(response.getMsg());
        }
    }

    public void forgetRestPassword(String username, String passwordNew, String forgetToken) {
        System.out.println(StringUtils.isBlank(forgetToken));
        if (StringUtils.isBlank(forgetToken) || StringUtils.isBlank(username) || StringUtils.isBlank(passwordNew)) {
            System.out.println(BaseConstant.MSG_PARAMETER_ERROR);
            return;
        }
        ServerResponse response = iUserService.forgetRestPassword(username, passwordNew, forgetToken);
        System.out.println(response.getMsg());
    }

    @Test
    public void queryUserInformation() {
        ServerResponse response = iUserService.queryUserInformation(1);
        if (response.isSuccess()) {
            System.out.println(ToStringBuilder.reflectionToString(response.getData(), ToStringStyle.JSON_STYLE));
        }
    }

    public void updateUserInformation(User user) {
        //更新用户信息，id不能更改，还有用户名不能更改，在web环境下，从Session之中获取用户名和id
        user.setId(1);
        user.setUsername("admin");
        ServerResponse<User> response = iUserService.updateUserInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(user.getUsername());
            System.out.println(response.getMsg());
            System.out.println("update after :" + ToStringBuilder.reflectionToString(response.getData(), ToStringStyle.JSON_STYLE));
        }
    }


    @Override
    public void after() {
        super.after();
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.isBlank(" "));
    }
}
