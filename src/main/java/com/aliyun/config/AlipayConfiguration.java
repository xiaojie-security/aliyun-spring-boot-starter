package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.v3.ApiException;
import com.aliyun.core.pay.*;
import com.aliyun.model.AliPayDetails;
import com.aliyun.properties.AliyunProperties;
import com.aliyun.properties.pojo.AliyunPay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云支付配置。
 */
@Configuration
@RequiredArgsConstructor
public class AlipayConfiguration implements InitializingBean {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    private final AliyunProperties aliyunProperties;
    private AliyunPay aliyunPay;

    @Override
    public void afterPropertiesSet() throws Exception {
        aliyunPay = aliyunProperties.getPay();
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.scan-code", name = "enable", havingValue = "true")
    public com.alipay.v3.ApiClient scanCodePayClient() {
        AliPayDetails scanCode = aliyunPay.getScanCode();
        com.alipay.v3.ApiClient apiClient = com.alipay.v3.Configuration.getDefaultApiClient();
        try {
            com.alipay.v3.util.model.AlipayConfig alipayConfig = new com.alipay.v3.util.model.AlipayConfig();
            if (scanCode != null) {
                alipayConfig.setServerUrl(scanCode.getGateWay());
                alipayConfig.setAppId(scanCode.getAppId());
                alipayConfig.setPrivateKey(scanCode.getPrivateKey());
                alipayConfig.setAlipayPublicKey(scanCode.getPublicKey());
            }
            apiClient.setAlipayConfig(alipayConfig);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return apiClient;
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.scan-code", name = "enable", havingValue = "true")
    public AliPayScanCodeService scanCodeAliyunPayService(com.alipay.v3.ApiClient scanCodePayClient) {
        return new AliPayScanCodeService(scanCodePayClient,aliyunPay.getScanCode());
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.app", name = "enable", havingValue = "true")
    public AliPayAppService appAliPayService() throws AlipayApiException {
        return new AliPayAppService(new DefaultAlipayClient(createAlipayConfig(aliyunPay.getApp())),aliyunPay.getApp());
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.oauth2", name = "enable", havingValue = "true")
    public AliPayOAuth2Service aliPayOAuth2Service() throws AlipayApiException {
        return new AliPayOAuth2Service(new DefaultAlipayClient(createAlipayConfig(aliyunPay.getOauth())), aliyunPay.getOauth());
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.fund", name = "enable", havingValue = "true")
    public AlipayFundService alipayFundService() throws AlipayApiException{
        return new AlipayFundService(new DefaultAlipayClient(createAlipayConfig(aliyunPay.getFund())), aliyunPay.getFund());
    }



    public com.alipay.api.AlipayConfig createAlipayConfig(AliPayDetails details) {
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        if (details != null) {
            alipayConfig.setServerUrl(details.getGateWay());
            alipayConfig.setAppId(details.getAppId());
            alipayConfig.setFormat(FORMAT);
            alipayConfig.setPrivateKey(details.getPrivateKey());
            if (details.isCertificates()) {
                alipayConfig.setAppCertPath(details.getAppCertPath());
                alipayConfig.setAlipayPublicCertPath(details.getAlipayPublicCertPath());
                alipayConfig.setRootCertPath(details.getRootCertPath());
            }
            alipayConfig.setAlipayPublicKey(details.getPublicKey());
            alipayConfig.setCharset(CHARSET);
            alipayConfig.setSignType(SIGN_TYPE);
        }
        return alipayConfig;
    }



}
