package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.pay.AliPayOAuth2Service;
import com.aliyun.model.AliPayDetails;
import com.aliyun.properties.AliPayOAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云支付配置。
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.pay.oauth2", name = "enable", havingValue = "true")
public class AlipayOAuth2Configuration {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    private final AliPayOAuth2Properties aliPayOAuth2Properties;

    
    @Bean
    @ConditionalOnMissingBean(AliPayOAuth2Service.class)
    public AliPayOAuth2Service aliPayOAuth2Service() throws AlipayApiException {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(aliPayOAuth2Properties.getGateWay());
        alipayConfig.setAppId(aliPayOAuth2Properties.getAppId());
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setPrivateKey(aliPayOAuth2Properties.getPrivateKey());
        if (aliPayOAuth2Properties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayOAuth2Properties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayOAuth2Properties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayOAuth2Properties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayOAuth2Properties.getPublicKey());
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return new AliPayOAuth2Service(new DefaultAlipayClient(alipayConfig), aliPayOAuth2Properties.getAliPayDetails());
    }
}
