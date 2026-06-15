package com.aliyun.model;

import com.aliyun.properties.pojo.AliyunBaseProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AliPayDetails extends AliyunBaseProperties {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 网关地址
     */
    private String gateWay;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 应用公钥证书路径
     */
    private String appCertPath;

    /**
     * 支付宝公钥证书路径
     */
    private String alipayPublicCertPath;

    /**
     * 支付宝根证书路径
     */
    private String rootCertPath;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 订单有效时间（单位：毫秒）
     */
    private Long validityTime;

    /**
     * 证书模式
     */
    private boolean certificates;
}

