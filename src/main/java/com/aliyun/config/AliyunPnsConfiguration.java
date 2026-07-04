package com.aliyun.config;

import com.aliyun.core.pns.AliyunPnsService;
import com.aliyun.core.pns.impl.DefaultAliyunPnsService;
import com.aliyun.credentials.Client;
import com.aliyun.model.AliyunCredential;
import com.aliyun.properties.AliyunProperties;
import com.aliyun.properties.pojo.AliyunPns;
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
public class AliyunPnsConfiguration extends AliyunBaseConfiguration implements InitializingBean {

    private final AliyunProperties aliyunProperties;
    private AliyunPns pns;
    private AliyunCredential credential;

    @Override
    public void afterPropertiesSet() throws Exception {
        pns = aliyunProperties.getPns();
        credential = aliyunProperties.createStsCredential(pns.getRamRoleArn());
    }

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
