package com.aliyun.config;

import com.aliyun.core.alipay.oauth2.AliPayOAuth2Service;
import com.aliyun.core.alipay.oauth2.impl.DefaultAliPayOAuth2Service;
import com.aliyun.provider.AlipayConfigProvider;
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
     */
    @Bean
    @ConditionalOnMissingBean(AliPayOAuth2Service.class)
    public AliPayOAuth2Service aliPayOAuth2Service(AlipayConfigProvider provider) {
        return new DefaultAliPayOAuth2Service(provider);
    }
}
