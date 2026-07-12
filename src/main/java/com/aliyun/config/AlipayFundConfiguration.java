package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.pay.AliPayOAuth2Service;
import com.aliyun.core.pay.AlipayFundService;
import com.aliyun.model.AliPayDetails;
import com.aliyun.properties.AliPayFundProperties;
import com.aliyun.properties.AliPayOAuth2Properties;
import com.aliyun.properties.pojo.AliPay;
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
@ConditionalOnProperty(prefix = "aliyun.pay.fund", name = "enable", havingValue = "true")
public class AlipayFundConfiguration {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    private final AliPayFundProperties aliPayFundProperties;
    
    @Bean
    @ConditionalOnMissingBean(AlipayFundService.class)
    public AlipayFundService alipayFundService() throws AlipayApiException {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(aliPayFundProperties.getGateWay());
        alipayConfig.setAppId(aliPayFundProperties.getAppId());
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setPrivateKey(aliPayFundProperties.getPrivateKey());
        if (aliPayFundProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayFundProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayFundProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayFundProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayFundProperties.getPublicKey());
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return new AlipayFundService(new DefaultAlipayClient(alipayConfig), aliPayFundProperties.getAliPayDetails());
    }

}
