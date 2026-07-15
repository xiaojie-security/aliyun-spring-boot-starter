package com.aliyun.config;

import com.aliyun.credentials.Client;
import com.aliyun.config.domain.AliyunCredential;

import java.util.UUID;

/**
 * 阿里云配置公共父类。
 * 统一封装 STS 凭证与 OpenAPI 配置构造逻辑，避免子配置类重复实现。
 */
public abstract class AliyunBaseConfiguration {

    protected static final String RAM_ROLE_ARN = "ram_role_arn";

    /**
     * 创建阿里云凭证配置。
     *
     * @param credential STS 凭证配置
     * @return 阿里云凭证配置对象
     */
    protected com.aliyun.credentials.models.Config createCredentialConfig(AliyunCredential credential) {
        return new com.aliyun.credentials.models.Config()
                .setType(RAM_ROLE_ARN)
                .setAccessKeyId(credential.getAccessKeyId())
                .setAccessKeySecret(credential.getAccessKeySecret())
                .setRoleArn(credential.getRamRoleArn())
                .setRoleSessionName(UUID.randomUUID().toString())
                .setRoleSessionExpiration(Math.toIntExact(credential.getExpire()));
    }

    /**
     * 创建 OpenAPI 配置。
     *
     * @param credential STS 凭证配置
     * @return OpenAPI 配置对象
     * @throws Exception 创建凭证客户端异常
     */
    protected com.aliyun.teaopenapi.models.Config createOpenApiConfig(AliyunCredential credential) throws Exception {
        return new com.aliyun.teaopenapi.models.Config()
                .setCredential(new Client(createCredentialConfig(credential)));
    }


}
