package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.v3.ApiException;
import com.aliyun.core.pay.AlipayFundService;
import com.aliyun.core.pay.AppAliPayService;
import com.aliyun.core.pay.ScanCodeAliPayService;
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
    public ScanCodeAliPayService scanCodeAliyunPayService(com.alipay.v3.ApiClient scanCodePayClient) {
        return new ScanCodeAliPayService(scanCodePayClient,aliyunPay.getScanCode());
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.app", name = "enable", havingValue = "true")
    public AppAliPayService appAliPayService() throws AlipayApiException {
        AliPayDetails app = aliyunPay.getApp();
        AlipayConfig alipayConfig = new AlipayConfig();
        if (app != null) {
            alipayConfig.setServerUrl(app.getGateWay());
            alipayConfig.setAppId(app.getAppId());
            alipayConfig.setFormat(FORMAT);
            alipayConfig.setAlipayPublicKey(app.getPublicKey());
            alipayConfig.setPrivateKey(app.getPrivateKey());
            alipayConfig.setAppCertPath(app.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(app.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(app.getRootCertPath());
            alipayConfig.setCharset(CHARSET);
            alipayConfig.setSignType(SIGN_TYPE);
        }
        return new AppAliPayService(new DefaultAlipayClient(alipayConfig),aliyunPay.getApp());
    }

    @Bean
    @ConditionalOnProperty(prefix = "aliyun.pay.fund", name = "enable", havingValue = "true")
    public AlipayFundService alipayFundService() throws AlipayApiException{
        AliPayDetails fund = aliyunPay.getFund();
        AlipayConfig alipayConfig = new AlipayConfig();
        if (fund != null) {
            alipayConfig.setServerUrl(fund.getGateWay());
            alipayConfig.setAppId(fund.getAppId());
            alipayConfig.setFormat(FORMAT);
            alipayConfig.setAlipayPublicKey(fund.getPublicKey());
            alipayConfig.setPrivateKey(fund.getPrivateKey());
            alipayConfig.setAppCertPath(fund.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(fund.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(fund.getRootCertPath());
            alipayConfig.setCharset(CHARSET);
            alipayConfig.setSignType(SIGN_TYPE);
        }
        return new AlipayFundService(new DefaultAlipayClient(alipayConfig));
    }



}
