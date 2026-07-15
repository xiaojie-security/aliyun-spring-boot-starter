package com.aliyun.config;

import com.aliyun.core.sms.AliyunSmsService;
import com.aliyun.config.domain.AliyunCredential;
import com.aliyun.properties.AliyunSmsProperties;
import com.aliyun.properties.AliyunStsProperties;
import com.aliyun.teaopenapi.models.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云号码认证配置。
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.sms", name = "enable", havingValue = "true")
public class AliyunSmsConfiguration extends AliyunBaseConfiguration {

    private final AliyunSmsProperties sms;
    private final AliyunCredential credential;

    @Bean("aliyunSmsClient")
    @ConditionalOnMissingBean(com.aliyun.dysmsapi20170525.Client.class)
    public com.aliyun.dysmsapi20170525.Client client() throws Exception{
        Config config = createOpenApiConfig(credential);
        config.endpoint = sms.getEndpoint();
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    @Bean
    @ConditionalOnMissingBean(AliyunSmsService.class)
    public AliyunSmsService aliyunSmsService(com.aliyun.dysmsapi20170525.Client aliyunSmsClient) {
        return new AliyunSmsService(sms, aliyunSmsClient);
    }
}
