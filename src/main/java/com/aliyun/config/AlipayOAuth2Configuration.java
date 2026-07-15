package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.alipay.oauth2.AliPayOAuth2Service;
import com.aliyun.core.alipay.oauth2.impl.DefaultAliPayOAuth2Service;
import com.aliyun.properties.AliPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云支付配置。
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.pay.oauth2", name = "enable", havingValue = "true")
public class AlipayOAuth2Configuration {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    private final AliPayProperties aliPayProperties;

    /**
     * 装配支付宝 OAuth2 服务。
     *
     * @return OAuth2 服务
     * @throws AlipayApiException 支付宝客户端初始化异常
     */
    @Bean
    @ConditionalOnMissingBean(AliPayOAuth2Service.class)
    public AliPayOAuth2Service aliPayOAuth2Service() throws AlipayApiException {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(aliPayProperties.getGateWay());
        alipayConfig.setAppId(aliPayProperties.getAppId());
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setPrivateKey(aliPayProperties.getPrivateKey());
        if (aliPayProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayProperties.getPublicKey());
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return new DefaultAliPayOAuth2Service(new DefaultAlipayClient(alipayConfig), aliPayProperties.getAliPayDetails());
    }
}
