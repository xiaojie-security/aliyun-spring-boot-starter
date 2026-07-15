package com.aliyun.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.core.alipay.transfer.AlipayTransferService;
import com.aliyun.core.alipay.transfer.impl.DefaultAlipayTransferService;
import com.aliyun.properties.AlipayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "alipay.transfer", name = "enable", havingValue = "true")
public class AlipayTransferConfiguration {


    /**
     * 装配支付宝转账服务。
     *
     * @return 转账服务
     * @throws AlipayApiException 支付宝客户端初始化异常
     */
    @Bean
    @ConditionalOnMissingBean(AlipayTransferService.class)
    public AlipayTransferService alipayTransferService(com.alipay.api.AlipayConfig alipayConfig) throws AlipayApiException {
        return new DefaultAlipayTransferService(new DefaultAlipayClient(alipayConfig));
    }
}
