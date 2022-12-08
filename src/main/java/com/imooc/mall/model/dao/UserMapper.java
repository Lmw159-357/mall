package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据userName，查询user对象
     * @param username
     * @return
     */
    User selectByName(String username);

    /**
     * 登录时使用：根据用户名和密码两个条件，查询User
     * @param userName
     * @param password
     * @return
     */
    User selectLogin(@Param("userName") String userName, @Param("password")String password);
}