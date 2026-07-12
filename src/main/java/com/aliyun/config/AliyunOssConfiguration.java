package com.aliyun.config;

import com.aliyun.core.oss.AliyunOssService;
import com.aliyun.core.oss.impl.DefaultAliyunOssService;
import com.aliyun.credentials.models.CredentialModel;
import com.aliyun.config.domain.AliyunCredential;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.properties.AliyunOssProperties;
import com.aliyun.properties.AliyunStsProperties;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.Credentials;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProviderSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 OSS 配置。
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aliyun.oss", name = "enable", havingValue = "true")
public class AliyunOssConfiguration extends AliyunBaseConfiguration {

    private final AliyunOssProperties oss;
    private final AliyunCredential credential;


    @Bean
    @ConditionalOnMissingBean(OSSClient.class)
    public OSSClient ossV2Client() throws Exception {
        if (oss == null) {
            return null;
        }
        com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client(
                createCredentialConfig(credential));

        CredentialsProvider credentialsProviderV2 = new CredentialsProviderSupplier(() -> {
            try {
                CredentialModel credential = credentialClient.getCredential();
                return new Credentials(
                        credential.getAccessKeyId(),
                        credential.getAccessKeySecret(),
                        credential.getSecurityToken()
                );
            } catch (Exception e) {
                throw new RuntimeException("获取凭证失败", e);
            }
        });

        return OSSClient.newBuilder()
                .credentialsProvider(credentialsProviderV2)
                .region(oss.getRegion())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(com.aliyun.oss.OSS.class)
    public com.aliyun.oss.OSS ossClient() throws Exception {
        if (oss == null) {
            return null;
        }

        com.aliyun.credentials.Client credentialClient = new com.aliyun.credentials.Client(
                createCredentialConfig(credential));
        com.aliyun.oss.common.auth.CredentialsProvider credentialsProvider = new com.aliyun.oss.common.auth.CredentialsProvider() {
            @Override
            public void setCredentials(com.aliyun.oss.common.auth.Credentials credentials) {
            }

            @Override
            public com.aliyun.oss.common.auth.Credentials getCredentials() {
                CredentialModel credential = credentialClient.getCredential();
                return new DefaultCredentials(
                        credential.getAccessKeyId(),
                        credential.getAccessKeySecret(),
                        credential.getSecurityToken()
                );
            }
        };

        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        return OSSClientBuilder.create()
                .endpoint(oss.getEndpoint())
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(oss.getRegion())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(AliyunOssService.class)
    public AliyunOssService aliyunOssService(OSSClient ossV2Client, com.aliyun.oss.OSS ossClient) {
        return new DefaultAliyunOssService(ossV2Client, ossClient, oss);
    }
}
