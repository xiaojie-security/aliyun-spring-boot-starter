package com.aliyun.properties.pojo;

import com.aliyun.model.AliPayDetails;
import lombok.Data;

/**
 * 阿里云支付配置类
 *
 * <p>用于封装支付宝支付相关配置信息，包括应用ID、网关地址、密钥等</p>
 */
@Data
public class AliyunPay {

    /**
     * app支付
     */
    private AliPayDetails app;

    /**
     * 资金支付
     */
    private AliPayDetails fund;

    /**
     * 扫码支付
     */
    private AliPayDetails scanCode;

    /**
     * oauth授权认证
     */
    private AliPayDetails oauth;
}

