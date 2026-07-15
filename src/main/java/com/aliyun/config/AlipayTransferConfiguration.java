package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.alipay.transfer.AlipayTransferService;
import com.aliyun.core.alipay.transfer.impl.DefaultAlipayTransferService;
import com.aliyun.properties.AliPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.pay.transfer", name = "enable", havingValue = "true")
public class AlipayTransferConfiguration {

    public static final String FORMAT = "json";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";

    private final AliPayProperties aliPayProperties;

    /**
     * 装配支付宝转账服务。
     *
     * @return 转账服务
     * @throws AlipayApiException 支付宝客户端初始化异常
     */
    @Bean
    @ConditionalOnMissingBean(AlipayTransferService.class)
    public AlipayTransferService alipayTransferService() throws AlipayApiException {
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
        return new DefaultAlipayTransferService(new DefaultAlipayClient(alipayConfig), aliPayProperties.getAliPayDetails());
    }
}
