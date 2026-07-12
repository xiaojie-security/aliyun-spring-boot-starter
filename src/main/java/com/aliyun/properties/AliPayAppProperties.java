package com.aliyun.properties;


import com.aliyun.model.AliPayDetails;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "aliyun.pay.app")
@Component
@Slf4j
@Setter
public class AliPayAppProperties extends AliPayBaseProperties {

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
    private Boolean certificates;

    @Override
    public String getAppId() {
        return appId != null ? appId : super.getAppId();
    }

    @Override
    public String getGateWay() {
        return gateWay != null ? gateWay : super.getGateWay();
    }

    @Override
    public String getPrivateKey() {
        return privateKey != null ? privateKey : super.getPrivateKey();
    }

    @Override
    public String getPublicKey() {
        return publicKey != null ? publicKey : super.getPublicKey();
    }

    @Override
    public String getAppCertPath() {
        return appCertPath != null ? appCertPath : super.getAppCertPath();
    }

    @Override
    public String getAlipayPublicCertPath() {
        return alipayPublicCertPath != null ? alipayPublicCertPath : super.getAlipayPublicCertPath();
    }

    @Override
    public String getRootCertPath() {
        return rootCertPath != null ? rootCertPath : super.getRootCertPath();
    }

    @Override
    public String getSellerId() {
        return sellerId != null ? sellerId : super.getSellerId();
    }

    @Override
    public Long getValidityTime() {
        return validityTime != null ? validityTime : super.getValidityTime();
    }

    @Override
    public Boolean getCertificates() {
        return certificates != null ? certificates : super.getCertificates();
    }

    @Override
    public boolean isCertificates() {
        // 优先使用子类的certificates判断，如果为null则使用父类的
        Boolean certValue = certificates != null ? certificates : super.getCertificates();
        return Boolean.TRUE.equals(certValue);
    }

    @Override
    public AliPayDetails getAliPayDetails() {
        AliPayDetails aliPayDetails = new AliPayDetails();
        aliPayDetails.setAppId(getAppId());
        aliPayDetails.setGateWay(getGateWay());
        aliPayDetails.setPrivateKey(getPrivateKey());
        aliPayDetails.setPublicKey(getPublicKey());
        aliPayDetails.setRootCertPath(getRootCertPath());
        aliPayDetails.setAppCertPath(getAppCertPath());
        aliPayDetails.setAlipayPublicCertPath(getAlipayPublicCertPath());
        aliPayDetails.setSellerId(getSellerId());
        aliPayDetails.setValidityTime(getValidityTime());
        aliPayDetails.setCertificates(getCertificates());
        return aliPayDetails;
    }


}
