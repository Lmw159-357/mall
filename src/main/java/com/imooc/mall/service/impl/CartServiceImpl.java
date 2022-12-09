package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：购物车模块的Service实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;


    /**
     * 根据前端要求，根据cart表和product表，获取并组织购物车列表数据
     * @return
     */
    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOList = cartMapper.selectList(userId);
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO =  cartVOList.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOList;
    }

    /**
     * 添加商品到购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        //调用工具方法，先看下【我们传的这个productId，在数据库中究竟有没有这个id的商品】；
        //然后，再看下商品是否是上架状态、商品库存是否足够；
        validProduct(productId, count);

        //然后，在看下购物车中，是否已经添加过了这个商品
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {//如果该商品之前，不在购物车里；那么，我们就创建一个cart记录，添加到cart表中；
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.CartIsSelected.CHECKED);//默认情况下，商品被添加到购物车后，就是选中状态
            cartMapper.insertSelective(cart);
        } else {//如果该商品之前，就在购物车里了；那么，我们就去更新购物车中的这个商品，主要是对商品数量的叠加
            int countNew = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(countNew);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            //至于商品是否设为选中，需要看具体的项目业务要求；
            // 我们这儿的策略，只要我们增加购物车的某个商品的数量了，就认为用户想买了，就把其设为选中状态;
            cartNew.setSelected(Constant.CartIsSelected.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }

        return this.list(userId);
    }

    /**
     * 工具方法；判断商品是否存在、商品是否是上架状态、商品库存是否足够；
     * @param productId
     * @param count
     */
    private void validProduct(Integer productId, Integer count) {
        //判断商品是否存在、商品是否是上架状态；如果不行，就抛出“商品不可售”的异常；
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || !product.getStatus().equals(Constant.SaleStatus.SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        //判断商品库存，如果库存不足，也不能把商品添加到购物车，抛出“商品库存不足异常；
        if (count > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }

    /**
     * 更新购物车中，某商品的数量；
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        //更新的时候，我们也要检查下：【前台传过来的productId，对应的商品是否存在】、【商品是否是上架状态】、【商品库存是否足够】
        validProduct(productId, count);

        //然后，在看下购物车中，是否已经添加过了这个商品；；；理论上，既然是更新嘛，购物车中应该是已经添加过这个商品的；
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //所以，如果购物车中没有这个商品；这就表示是有问题的；那么我们就不进行这个更新操作；返回“更新失败”异常；
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        } else {
            //如果购物车中已经有了这个商品；那么我们就去更新购物车中该商品的数量；
            // 也就是说把该商品在购物车中的数量，更改为我们传入的数量count
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.CartIsSelected.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    /**
     * 删除购物车中的某个商品
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {

        //首先，根据userId和productId，去查一下，看购物车中是否有对应的数据
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //所以，如果购物车中没有这个商品；那么很显然，是无法删除的(都没有，怎么删除……);于是，就抛出“删除失败”异常；
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_ERROR);
        } else {
            //如果购物车中有这个商品，那么我们就可以正常去删除了
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    /**
     * 选中/不选中购物车的某个商品
     * @param userId
     * @param productId
     * @param selected
     * @return
     */
    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        //首先，根据userId和productId，去查一下，看购物车中是否有对应的数据
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //所以，如果购物车中没有这个商品；那么很显然，是无法选中(都没有，怎么更新其selected字段的值);于是，就抛出“更新失败”异常；
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        } else {
            //如果购物车中有这个商品，那么我们就可以正常去更新其selected字段了
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);
    }

    /**
     * 全选/全不选购物车的商品
     * @param userId
     * @param selected
     * @return
     */
    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        //直接，更新该用户，在购物车中的，所有商品的selected状态；（因为是全部更新，所以这儿我们完全不需要先查看有没有，直接更新就是了）
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }
}