package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.alipay.oauth2.AliPayOAuth2Service;
import com.aliyun.core.alipay.oauth2.impl.DefaultAliPayOAuth2Service;
import com.aliyun.properties.AlipayProperties;
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
@ConditionalOnProperty(prefix = "alipay.oauth2", name = "enable", havingValue = "true")
public class AlipayOAuth2Configuration {

    /**
     * 装配支付宝 OAuth2 服务。
     *
     * @return OAuth2 服务
     * @throws AlipayApiException 支付宝客户端初始化异常
     */
    @Bean
    @ConditionalOnMissingBean(AliPayOAuth2Service.class)
    public AliPayOAuth2Service aliPayOAuth2Service(com.alipay.api.AlipayConfig alipayConfig) throws AlipayApiException {
        return new DefaultAliPayOAuth2Service(new DefaultAlipayClient(alipayConfig));
    }
}
