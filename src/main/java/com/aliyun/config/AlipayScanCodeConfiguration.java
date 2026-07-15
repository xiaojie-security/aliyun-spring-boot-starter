package com.aliyun.config;

import com.alipay.v3.ApiException;
import com.aliyun.core.alipay.payment.AliPayScanCodeService;
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
@ConditionalOnProperty(prefix = "aliyun.pay.scan-code", name = "enable", havingValue = "true")
public class AlipayScanCodeConfiguration {

    private final AliPayProperties aliPayProperties;

    @Bean
    @ConditionalOnMissingBean(AliPayScanCodeService.class)
    public AliPayScanCodeService scanCodeAliyunPayService()  throws ApiException{
        com.alipay.v3.ApiClient apiClient = com.alipay.v3.Configuration.getDefaultApiClient();
        com.alipay.v3.util.model.AlipayConfig alipayConfig = new com.alipay.v3.util.model.AlipayConfig();
        alipayConfig.setServerUrl(aliPayProperties.getGateWay());
        alipayConfig.setAppId(aliPayProperties.getAppId());
        alipayConfig.setPrivateKey(aliPayProperties.getPrivateKey());
        if (aliPayProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayProperties.getPublicKey());
        apiClient.setAlipayConfig(alipayConfig);
        return new AliPayScanCodeService(apiClient);
    }
}
