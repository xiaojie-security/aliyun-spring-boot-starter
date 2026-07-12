package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.v3.ApiException;
import com.aliyun.core.pay.AliPayAppService;
import com.aliyun.core.pay.AliPayOAuth2Service;
import com.aliyun.core.pay.AliPayScanCodeService;
import com.aliyun.core.pay.AlipayFundService;
import com.aliyun.model.AliPayDetails;
import com.aliyun.properties.AliPayAppProperties;
import com.aliyun.properties.pojo.AliPay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
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
    private final AliPayAppProperties aliPayAppProperties;


    @Bean
    @ConditionalOnMissingBean(AliPayAppService.class)
    public AliPayAppService appAliPayService() throws AlipayApiException {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(aliPayAppProperties.getGateWay());
        alipayConfig.setAppId(aliPayAppProperties.getAppId());
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setPrivateKey(aliPayAppProperties.getPrivateKey());
        if (aliPayAppProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayAppProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayAppProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayAppProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayAppProperties.getPublicKey());
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return new AliPayAppService(new DefaultAlipayClient(alipayConfig), aliPayAppProperties.getAliPayDetails());
    }
}
