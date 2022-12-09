package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 描述：【购物车】模块的Controller
 */

@RestController
//@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 购物车模块：购物车列表
     * @return
     */
    @ApiOperation("购物车列表")
    @GetMapping("/list")
    public ApiRestResponse list() {
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车模块：添加商品到购物车；
     * @param productId
     * @param count
     * @return
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车模块：更新购物车某个商品的数量
     * @param productId
     * @param count
     * @return
     */
    @ApiOperation("更新购物车某个商品的数量")
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车模块：删除购物车的某个商品
     * @param productId
     * @return
     */
    @ApiOperation("删除购物车的某个商品")
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam("productId") Integer productId) {
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车模块：选中/不选中购物车的某个商品
     * @param productId
     * @param selected
     * @return
     */
    @ApiOperation("选中/不选中购物车的某个商品")
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam("productId") Integer productId,
                                  @RequestParam("selected") Integer selected) {
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车模块：全选/全不选购物车的商品
     * @param selected
     * @return
     */
    @ApiOperation("全选/全不选购物车的商品")
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam("selected") Integer selected) {
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}