package com.aliyun.config;

import com.aliyun.core.pns.AliyunPnsService;
import com.aliyun.core.pns.impl.DefaultAliyunPnsService;
import com.aliyun.credentials.Client;
import com.aliyun.config.domain.AliyunCredential;
import com.aliyun.properties.AliyunPnsProperties;
import com.aliyun.properties.AliyunStsProperties;
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
@ConditionalOnProperty(prefix = "aliyun.pns", name = "enable", havingValue = "true")
public class AliyunPnsConfiguration extends AliyunBaseConfiguration {

    private final AliyunPnsProperties pns;
    private final AliyunCredential credential;


    @Bean("aliyunPnsClient")
    @ConditionalOnMissingBean(com.aliyun.dypnsapi20170525.Client.class)
    public com.aliyun.dypnsapi20170525.Client client() throws Exception {
        if (pns == null) {
            return null;
        }
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(new Client(createCredentialConfig(credential)))
                .setEndpoint(pns.getEndpoint())
                .setRegionId(pns.getRegion());

        return new com.aliyun.dypnsapi20170525.Client(config);
    }

    @Bean
    @ConditionalOnMissingBean(AliyunPnsService.class)
    public AliyunPnsService aliyunPnsService(com.aliyun.dypnsapi20170525.Client aliyunPnsClient) {
        return new DefaultAliyunPnsService(pns, aliyunPnsClient);
    }
}
