package com.aliyun.config;

import com.aliyun.core.sts.AliyunStsService;
import com.aliyun.properties.AliyunStsProperties;
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
@ConditionalOnProperty(prefix = "aliyun.sts", name = "enable", havingValue = "true")
public class AliyunStsConfiguration extends AliyunBaseConfiguration  {
    private final AliyunStsProperties sts;


    @Bean
    @ConditionalOnMissingBean(AliyunStsService.class)
    public AliyunStsService aliyunStsService() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.setAccessKeyId(sts.getAccessKeyId());
        config.setAccessKeySecret(sts.getAccessKeySecret());
        config.setEndpoint(sts.getEndpoint());
        return new AliyunStsService(new com.aliyun.sts20150401.Client(config));
    }

}
