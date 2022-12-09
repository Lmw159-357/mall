package com.imooc.mall.service;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartService {

    /**
     * 根据前端要求，根据cart表和product表，获取并组织购物车列表数据
     * @param userId
     * @return
     */
    List<CartVO> list(Integer userId);

    /**
     * 添加商品到购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    List<CartVO> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车中，某商品的数量；
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    List<CartVO> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车中的某个商品
     * @param userId
     * @param productId
     * @return
     */
    List<CartVO> delete(Integer userId, Integer productId);

    /**
     * 选中/不选中购物车的某个商品
     * @param userId
     * @param productId
     * @param selected
     * @return
     */
    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected);

    /**
     * 全选/全不选购物车的商品
     * @param userId
     * @param selected
     * @return
     */
    List<CartVO> selectAllOrNot(Integer userId, Integer selected);
}
