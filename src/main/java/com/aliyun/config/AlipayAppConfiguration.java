package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.alipay.payment.AliPayAppService;
import com.aliyun.properties.AliPayProperties;
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
@ConditionalOnProperty(prefix = "aliyun.pay.app", name = "enable", havingValue = "true")
public class AlipayAppConfiguration {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    private final AliPayProperties aliPayProperties;


    @Bean
    @ConditionalOnMissingBean(AliPayAppService.class)
    public AliPayAppService appAliPayService() throws AlipayApiException {
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
        return new AliPayAppService(new DefaultAlipayClient(alipayConfig), aliPayProperties.getAliPayDetails());
    }
}
