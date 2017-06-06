package com.scott.mmall.controller.backend;

import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ResponseCode;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.Category;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.ICategoryService;
import com.scott.mmall.services.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.applet.resources.MsgAppletViewer;

import javax.servlet.http.HttpSession;

/**
 * Created by Ubuntu on 2017/5/26.
 */
@Controller
@RequestMapping("/manager/category/")
public class CategoryManagerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorCode(ResponseCode.NO_PERMISSION.getCode(), ResponseCode.NO_PERMISSION.getDesc());
        }
        if (StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 更新一个类别结点的名称
     *
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("update_category_name.do")
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorCode(ResponseCode.NO_PERMISSION.getCode(), ResponseCode.NO_PERMISSION.getDesc());
        }
        if (StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage(BaseConstant.MSG_PARAMETER_ERROR);
        }
        return iCategoryService.updateCategory(categoryName, categoryId);
    }

    /**
     * 取得平级的类别结点
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorCode(ResponseCode.NO_PERMISSION.getCode(), ResponseCode.NO_PERMISSION.getDesc());
        }
        return iCategoryService.getChildParallelCategory(categoryId);
    }

    /**
     * 递归遍历类别
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorCode(ResponseCode.NO_PERMISSION.getCode(), ResponseCode.NO_PERMISSION.getDesc());
        }
        return iCategoryService.selectCategoryAndChildByCategoryId(categoryId);
    }
}
