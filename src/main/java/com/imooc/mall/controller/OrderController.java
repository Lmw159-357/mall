package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 描述：订单Controller
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 【前台：创建订单】接口；
     * @param createOrderReq
     * @return
     */
    @ApiOperation("创建订单")
    @PostMapping("/order/create")
    public ApiRestResponse createOrder(@Valid @RequestBody CreateOrderReq createOrderReq) {
        String orderNum = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNum);
    }

    /**
     * 【前台：订单详情】接口；
     * @param orderNo
     * @return
     */
    @ApiOperation("订单详情")
    @GetMapping("/order/detail")
    public ApiRestResponse detail(@RequestParam("orderNo") String orderNo) {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    /**
     * 【前台：订单列表】接口；
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("订单列表")
    @GetMapping("/order/list")
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize) {
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 【前台：取消订单】接口；
     * @param orderNo
     * @return
     */
    @ApiOperation("取消订单")
    @PostMapping("/order/cancel")
    public ApiRestResponse cancel(@RequestParam("orderNo") String orderNo) {
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 【前台：生成支付二维码】接口;
     * @param orderNo
     * @return
     */
    @ApiOperation("生成支付二维码")
    @PostMapping("/order/qrcode")
    public ApiRestResponse qrcode(@RequestParam("orderNo") String orderNo) {
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    /**
     * 【前台：支付订单】接口;
     * @param orderNo
     * @return
     */
    @ApiOperation("支付订单")
    @GetMapping("/pay")
    public ApiRestResponse pay(@RequestParam("orderNo") String orderNo) {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

}