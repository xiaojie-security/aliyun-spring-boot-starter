package com.aliyun.properties.pojo;

import com.aliyun.model.AliPayDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 阿里云支付配置类
 *
 * <p>用于封装支付宝支付相关配置信息，包括应用ID、网关地址、密钥等</p>
 */
@EqualsAndHashCode(callSuper = true)
@Setter
public class AliPay extends AliPayDetails {

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

    /**
     * 获取合并后的 APP 支付配置。
     *
     * @return APP 支付配置
     */
    public AliPayDetails getApp() {
        return mergeDetails(app);
    }

    /**
     * 获取合并后的资金支付配置。
     *
     * @return 资金支付配置
     */
    public AliPayDetails getFund() {
        return mergeDetails(fund);
    }

    /**
     * 获取合并后的扫码支付配置。
     *
     * @return 扫码支付配置
     */
    public AliPayDetails getScanCode() {
        return mergeDetails(scanCode);
    }

    /**
     * 获取合并后的 OAuth 配置。
     *
     * @return OAuth 配置
     */
    public AliPayDetails getOauth() {
        return mergeDetails(oauth);
    }

    /**
     * 合并公共配置与子配置。
     *
     * @param childDetails 子配置
     * @return 合并后的配置
     */
    private AliPayDetails mergeDetails(AliPayDetails childDetails) {
        if (childDetails == null) {
            return this;
        }
        AliPayDetails mergedDetails = new AliPayDetails();
        mergedDetails.setEnable(childDetails.getEnable() != null ? childDetails.getEnable() : getEnable());
        mergedDetails.setAccessKeyId(selectText(childDetails.getAccessKeyId(), getAccessKeyId()));
        mergedDetails.setAccessKeySecret(selectText(childDetails.getAccessKeySecret(), getAccessKeySecret()));
        mergedDetails.setAppId(selectText(childDetails.getAppId(), getAppId()));
        mergedDetails.setGateWay(selectText(childDetails.getGateWay(), getGateWay()));
        mergedDetails.setPrivateKey(selectText(childDetails.getPrivateKey(), getPrivateKey()));
        mergedDetails.setPublicKey(selectText(childDetails.getPublicKey(), getPublicKey()));
        mergedDetails.setAppCertPath(selectText(childDetails.getAppCertPath(), getAppCertPath()));
        mergedDetails.setAlipayPublicCertPath(selectText(childDetails.getAlipayPublicCertPath(), getAlipayPublicCertPath()));
        mergedDetails.setRootCertPath(selectText(childDetails.getRootCertPath(), getRootCertPath()));
        mergedDetails.setSellerId(selectText(childDetails.getSellerId(), getSellerId()));
        mergedDetails.setValidityTime(childDetails.getValidityTime() != null ? childDetails.getValidityTime() : getValidityTime());
        mergedDetails.setCertificates(childDetails.getCertificates() != null ? childDetails.getCertificates() : getCertificates());
        return mergedDetails;
    }

    /**
     * 从子配置与公共配置中选择文本值。
     *
     * @param childValue 子配置值
     * @param parentValue 公共配置值
     * @return 优先返回子配置，其次返回公共配置
     */
    private String selectText(String childValue, String parentValue) {
        return StringUtils.hasText(childValue) ? childValue : parentValue;
    }
}
