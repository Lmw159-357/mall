package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 根据订单号，查询订单
     * @param orderNo
     * @return
     */
    Order selectByOrderNo(String orderNo);

    /**
     * 根据userId，查询订单
     * @param userId
     * @return
     */
    List<Order> selectOrderForCustomer(Integer userId);

    /**
     * 查询所有订单
     * @return
     */
    List<Order> selectAllOrderForAdmin();
}