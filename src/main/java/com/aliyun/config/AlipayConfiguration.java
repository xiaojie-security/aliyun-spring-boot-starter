package com.aliyun.config;

import com.aliyun.properties.AlipayProperties;
import com.aliyun.provider.AlipayConfigProvider;
import com.aliyun.provider.PropertiesAlipayConfigProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 支付宝基础配置入口。
 * 仅负责启用配置属性绑定，具体客户端装配由各子配置类负责。
 */
@AutoConfiguration
public class AlipayConfiguration {

    /**
     * 注册支付宝全局配置 Bean。
     *
     * @return 支付宝全局配置
     */
    @Bean
    @ConditionalOnMissingBean(AlipayProperties.class)
    public AlipayProperties alipayProperties() {
        return new AlipayProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AlipayConfigProvider.class)
    public AlipayConfigProvider alipayConfigProvider(AlipayProperties alipayProperties) {
        return new PropertiesAlipayConfigProvider(alipayProperties);
    }
}
