package com.aliyun.config;

import com.aliyun.core.alipay.payment.AlipayPaymentService;
import com.aliyun.core.alipay.payment.impl.DefaultAlipayPaymentService;
import com.aliyun.provider.AlipayConfigProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.pay.payment", name = "enable", havingValue = "true")
public class AlipayPaymentConfiguration {

    /**
     * 装配支付宝支付服务。
     *
     * @return 支付服务
     */
    @Bean
    @ConditionalOnMissingBean(AlipayPaymentService.class)
    public AlipayPaymentService alipayPaymentService(AlipayConfigProvider provider) {
        return new DefaultAlipayPaymentService(provider);
    }
}
