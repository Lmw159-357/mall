package com.imooc.mall.utils;

import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 描述：订单号生成类
 */
public class OrderCodeFactory {
    //订单号有多种生成规则，主要是原则就是：防止重复；这儿我们采用【时间+随机数】的方式；

    /**
     * 以一定格式，获取当前时间
     * @return
     */
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 生成一个5位的随机数；（当然可以生成3、4、6、7、8……位；只是，这儿为了便于管理、程序明确，这儿我们统一生成5位）
     * @param n
     * @return
     */
    private static int getRandom(Long n) {
        Random random = new Random();
        return (int) (random.nextDouble() * (90000)) + 10000;
    }

    /**
     * 生成订单号
     * @param userId
     * @return
     */
    public static String getOrderCode(Long userId) {
        return getDateTime() + getRandom(userId);
    }
}