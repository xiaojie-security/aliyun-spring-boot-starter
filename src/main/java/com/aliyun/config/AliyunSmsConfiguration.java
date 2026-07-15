package com.aliyun.config;

import com.aliyun.core.sms.AliyunSmsService;
import com.aliyun.config.domain.AliyunCredential;
import com.aliyun.properties.AliyunSmsProperties;
import com.aliyun.teaopenapi.models.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云号码认证配置。
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.sms", name = "enable", havingValue = "true")
public class AliyunSmsConfiguration extends AliyunBaseConfiguration {

    private final AliyunSmsProperties sms;

    @Bean("aliyunSmsClient")
    @ConditionalOnMissingBean(com.aliyun.dysmsapi20170525.Client.class)
    public com.aliyun.dysmsapi20170525.Client client() throws Exception{
        AliyunCredential credential = createAliyunCredential(
                sms.getAccessKeyId(),
                sms.getAccessKeySecret(),
                sms.getRamRoleArn(),
                3600L
        );
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
