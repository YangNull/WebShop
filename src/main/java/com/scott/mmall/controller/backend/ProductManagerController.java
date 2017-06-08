package com.scott.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.scott.mmall.common.BaseConstant;
import com.scott.mmall.common.ResponseCode;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.Product;
import com.scott.mmall.pojo.User;
import com.scott.mmall.services.IFileService;
import com.scott.mmall.services.IProductService;
import com.scott.mmall.services.IUserService;
import com.scott.mmall.util.PropertiesUtil;
import com.scott.mmall.vo.ProductDetailVo;
import com.scott.mmall.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品管理Controller
 * Created by Ubuntu on 2017/5/28.
 */
@Controller
@RequestMapping("/manager/product/")
public class ProductManagerController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveProduct(ProductVo productVo, HttpSession session) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        //TODO checked productVo is valid
        //TODO productVO to Product
        Product product = new Product();
        return iProductService.saveOnUpdateProduct(product);
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        if (productId == null || status == null) {
            return ServerResponse.createByResponseCode(ResponseCode.ILLEGAL_ARGUMENT);
        }
        return iProductService.setSaleStatus(productId, status);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        if (productId == null) {
            return ServerResponse.createByResponseCode(ResponseCode.ILLEGAL_ARGUMENT);
        }
        return iProductService.managerProductDetail(productId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        return iProductService.productList(pageNum, pageSize);
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        if (StringUtils.isBlank(productName) && productId == null) {
            return ServerResponse.createByResponseCode(ResponseCode.ILLEGAL_ARGUMENT);
        }
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);

    }

    /**
     * 文件上传
     *
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByResponseCode(ResponseCode.NEED_LOGIN);
        }
        if (!checkUserIsAdmin(user)) {
            return ServerResponse.createByResponseCode(ResponseCode.NO_PERMISSION);
        }
        if (file == null) {
            return ServerResponse.createByResponseCode(ResponseCode.ILLEGAL_ARGUMENT);
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }


    /**
     * 富文本图片上传
     *
     * @param session
     * @param request
     * @param response
     * @param file
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImaUpload(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        Map resultMap = new HashMap();
        User user = (User) session.getAttribute(BaseConstant.KEY_CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", ResponseCode.NEED_LOGIN.getDesc());
            return resultMap;
        }
        if (!checkUserIsAdmin(user)) {
            resultMap.put("success", false);
            resultMap.put("msg", ResponseCode.NO_PERMISSION.getDesc());
            return resultMap;
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "文件上传失败");
            return resultMap;
        } else {
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", false);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        }
    }

    private boolean checkUserIsAdmin(User user) {

        return iUserService.checkAdmin(user).isSuccess();
    }
}
