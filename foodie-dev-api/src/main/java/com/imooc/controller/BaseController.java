package com.imooc.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class BaseController {
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    // 微信支付成功会通知 -> 支付中心接到通知后会通知 -> 本后台
    //                                        -> 回调通知的url
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    // 用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = "." + File.separator + "upload" +
                                                            File.separator + "faces";
    // public static final String IMAGE_USER_FACE_LOCATION = "./upload/faces";
}
