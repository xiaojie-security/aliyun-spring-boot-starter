package com.aliyun.properties;


import com.aliyun.model.AliPayDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "aliyun.pay")
@Component
@Data
public class AliPayProperties {

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

    /**
     * 是否启用证书模式。
     *
     * @return true-启用证书模式，false-未启用证书模式
     */
    public boolean isCertificates() {
        return Boolean.TRUE.equals(certificates);
    }

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
