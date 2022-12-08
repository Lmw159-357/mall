package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 描述：用户控制器
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    /**
     * 注册方法
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password) throws ImoocMallException {
        if (StringUtils.isEmpty(userName)) {//如果用户名为空，直接返回用户名不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {//如果密码为空，直接返回密码不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        if (password.length() < 8) {//如果密码长度小于8位，直接返回密码长度不能小于8位的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }

        userService.register(userName, password);
        return ApiRestResponse.success();
    }

    /**
     * 登录方法
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws ImoocMallException {
        if (StringUtils.isEmpty(userName)) {//如果用户名为空，直接返回用户名不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {//如果密码为空，直接返回密码不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        user.setPassword(null);//返回用户信息时候，去掉密码
        session.setAttribute(Constant.IMOOC_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     * @param session
     * @param signature
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUser(HttpSession session, @RequestParam("signature") String signature) throws ImoocMallException {
        //尝试，从Session中，获取User；
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {//如果Session中获取不到User，表示用户还没有登录，那么我们需要返回【需要登录】的错误提示信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }

        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     *退出登录
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录方法
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password,
                                      HttpSession session) throws ImoocMallException {
        if (StringUtils.isEmpty(userName)) {//如果用户名为空，直接返回用户名不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {//如果密码为空，直接返回密码不能为空的信息；
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        if (userService.checkAdminRole(user)) {//校验，用户是否是管理员；
            //如果是管理员，就执行后续操作
            user.setPassword(null);//返回用户信息时候，去掉密码
            session.setAttribute(Constant.IMOOC_MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {//如果不是管理员，就执行这种操作
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }
    }
}
