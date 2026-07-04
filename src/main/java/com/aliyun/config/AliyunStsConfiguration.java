package com.aliyun.config;

import com.aliyun.core.sts.AliyunStsService;
import com.aliyun.properties.AliyunProperties;
import com.aliyun.properties.pojo.AliyunSts;
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
@ConditionalOnProperty(prefix = "aliyun.sts", name = "enable", havingValue = "true")
public class AliyunStsConfiguration extends AliyunBaseConfiguration implements InitializingBean {
    private final AliyunProperties aliyunProperties;
    private AliyunSts sts;

    @Override
    public void afterPropertiesSet() throws Exception {
        sts = aliyunProperties.getSts();
    }

    @Bean
    @ConditionalOnMissingBean(com.aliyun.sts20150401.Client.class)
    public com.aliyun.sts20150401.Client client() throws Exception {
        if (sts == null) {
            return null;
        }
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.setAccessKeyId(sts.getAccessKeyId());
        config.setAccessKeySecret(sts.getAccessKeySecret());
        config.setEndpoint(sts.getEndpoint());
        return new com.aliyun.sts20150401.Client(config);
    }

    @Bean
    @ConditionalOnMissingBean(AliyunStsService.class)
    public AliyunStsService aliyunStsService(com.aliyun.sts20150401.Client client) {
        return new AliyunStsService(sts, client);
    }

}
