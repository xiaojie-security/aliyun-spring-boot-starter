package com.aliyun.config;

import com.aliyun.core.imm.AliyunImmService;
import com.aliyun.core.imm.impl.DefaultAliyunImmService;
import com.aliyun.config.domain.AliyunCredential;
import com.aliyun.properties.AliyunImmProperties;
import com.aliyun.properties.AliyunStsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 IMM 配置。
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.imm", name = "enable", havingValue = "true")
public class AliyunImmConfiguration extends AliyunBaseConfiguration {

    private final AliyunImmProperties imm;
    private final AliyunCredential credential;


    @Bean
    @ConditionalOnMissingBean(com.aliyun.imm20200930.Client.class)
    public com.aliyun.imm20200930.Client immClient() throws Exception {
        if (imm == null) {
            return null;
        }

        com.aliyun.teaopenapi.models.Config config = createOpenApiConfig(credential);
        config.setEndpoint(imm.getEndpointOverride());
        return new com.aliyun.imm20200930.Client(config);
    }

    @Bean
    @ConditionalOnMissingBean(AliyunImmService.class)
    public AliyunImmService aliyunImmService(com.aliyun.imm20200930.Client immClient) {
        return new DefaultAliyunImmService(imm, immClient);
    }
}
