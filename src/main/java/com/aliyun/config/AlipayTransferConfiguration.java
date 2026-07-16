package com.aliyun.config;

import com.aliyun.core.alipay.transfer.AlipayTransferService;
import com.aliyun.core.alipay.transfer.impl.DefaultAlipayTransferService;
import com.aliyun.provider.AlipayConfigProvider;
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
     */
    @Bean
    @ConditionalOnMissingBean(AlipayTransferService.class)
    public AlipayTransferService alipayTransferService(AlipayConfigProvider provider) {
        return new DefaultAlipayTransferService(provider);
    }
}
