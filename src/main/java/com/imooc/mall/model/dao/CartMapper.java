package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据userId，从cart表和product表中，查询购物车数据
     * @param userId
     * @return
     */
    List<CartVO> selectList(@Param("userId") Integer userId);

    /**
     * 根据userId和ProductId，从Cart表中，查询数据；
     * @param userId
     * @param productId
     * @return
     */
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    /**
     * 根据userId、ProductId、selected,更新购物车中某个商品的selected字段
     * @param userId
     * @param productId
     * @param selected
     * @return
     */
    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId,
                        @Param("selected") Integer selected);
}