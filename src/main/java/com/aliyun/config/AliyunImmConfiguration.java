package com.aliyun.config;

import com.aliyun.core.imm.AliyunImmService;
import com.aliyun.core.imm.impl.DefaultAliyunImmService;
import com.aliyun.model.AliyunCredential;
import com.aliyun.properties.AliyunProperties;
import com.aliyun.properties.pojo.AliyunImm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 IMM 配置。
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.imm", name = "enable", havingValue = "true")
public class AliyunImmConfiguration extends AliyunBaseConfiguration implements InitializingBean {

    private final AliyunProperties aliyunProperties;
    private AliyunImm imm;
    private AliyunCredential credential;

    @Override
    public void afterPropertiesSet() throws Exception {
        imm = aliyunProperties.getImm();
        credential = aliyunProperties.createStsCredential(imm.getRamRoleArn());
    }

    @Bean
    public com.aliyun.imm20200930.Client immClient() throws Exception {
        if (imm == null) {
            return null;
        }

        com.aliyun.teaopenapi.models.Config config = createOpenApiConfig(credential);
        config.setEndpoint(imm.getEndpointOverride());
        return new com.aliyun.imm20200930.Client(config);
    }

    @Bean
    public AliyunImmService aliyunImmService(com.aliyun.imm20200930.Client immClient) {
        return new DefaultAliyunImmService(imm, immClient);
    }
}
