package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.imooc.mall.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    /**
     * 获取User信息的方法
     * @return
     */
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(3);
    }

    /**
     * 注册方法
     * @param userName
     * @param password
     */
    @Override
    public void register(String userName, String password) throws ImoocMallException {
        //首先，查询用户名是否存在；注册时，不允许重名；
        User result = userMapper.selectByName(userName);
        if (result != null) { //如果result不为null，这说明已经有这个用户了；此时，就可以直接向上抛异常；
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //如果上面没有抛异常，那么说明用户名没有重名，那么我们就可以把这个注册信息写到数据库中
        User user = new User();
        user.setUsername(userName);
//        user.setPassword(password);
        try {
            user.setPassword(MD5Utils.getMD5String(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 登录方法
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    @Override
    public User login(String userName, String password) throws ImoocMallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5String(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    /**
     * 更新个性签名
     * @param user
     */
    @Override
    public void updateInformation(User user) throws ImoocMallException {
        int updateCount = userMapper.updateByPrimaryKeySelective(user);//更新操作，影响的记录数；正常情况下应该是1
        //如果更新的返回结果 > 1；那么表示，更新操作是不正确的。即更新操作是失败了的；
        if (updateCount > 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        return user.getRole().equals(2);//如果user的role=2，表示其是一个管理员用户；否则是普通用户
    }
}
