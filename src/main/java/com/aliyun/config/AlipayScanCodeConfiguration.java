package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.v3.ApiException;
import com.aliyun.core.pay.AliPayOAuth2Service;
import com.aliyun.core.pay.AliPayScanCodeService;
import com.aliyun.properties.AliPayOAuth2Properties;
import com.aliyun.properties.AliPayScanCodeProperties;
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
@ConditionalOnProperty(prefix = "aliyun.pay.scan-code", name = "enable", havingValue = "true")
public class AlipayScanCodeConfiguration {

    private final AliPayScanCodeProperties aliPayScanCodeProperties;

    @Bean
    @ConditionalOnMissingBean(AliPayScanCodeService.class)
    public AliPayScanCodeService scanCodeAliyunPayService()  throws ApiException{
        com.alipay.v3.ApiClient apiClient = com.alipay.v3.Configuration.getDefaultApiClient();
        com.alipay.v3.util.model.AlipayConfig alipayConfig = new com.alipay.v3.util.model.AlipayConfig();
        alipayConfig.setServerUrl(aliPayScanCodeProperties.getGateWay());
        alipayConfig.setAppId(aliPayScanCodeProperties.getAppId());
        alipayConfig.setPrivateKey(aliPayScanCodeProperties.getPrivateKey());
        if (aliPayScanCodeProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayScanCodeProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayScanCodeProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayScanCodeProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayScanCodeProperties.getPublicKey());
        apiClient.setAlipayConfig(alipayConfig);
        return new AliPayScanCodeService(apiClient);
    }
}
