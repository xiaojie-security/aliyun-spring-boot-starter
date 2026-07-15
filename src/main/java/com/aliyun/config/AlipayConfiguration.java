package com.aliyun.config;

import com.aliyun.properties.AliPayProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝基础配置入口。
 * 仅负责启用配置属性绑定，具体客户端装配由各子配置类负责。
 */
@Configuration
public class AlipayConfiguration {

    /**
     * 注册支付宝全局配置 Bean。
     *
     * @return 支付宝全局配置
     */
    @Bean
    @ConditionalOnMissingBean(AliPayProperties.class)
    public AliPayProperties aliPayProperties(){
        return new AliPayProperties();
    }

}
