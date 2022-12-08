package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.User;

/**
 * 描述：用户Service
 */
public interface UserService {
    /**
     * 获取User信息的方法
     * @return
     */
    User getUser();

    void register(String username, String password) throws ImoocMallException;

    /**
     * 登录方法
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    User login(String userName, String password) throws ImoocMallException;

    /**
     * 更新个性签名
     * @param user
     */
    void updateInformation(User user) throws ImoocMallException;

    /**
     * 判断某个用户是否为管理员用户
     * @param user
     * @return
     */
    boolean checkAdminRole(User user);
}
