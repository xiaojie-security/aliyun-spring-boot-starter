package com.aliyun.config.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AliyunCredential {

    /**
     * 阿里云访问密钥 ID
     * 用于标识用户的 AccessKey 身份
     */
    private String accessKeyId;

    /**
     * 阿里云访问密钥 Secret
     * 用于签名验证的密钥信息
     */
    private String accessKeySecret;

    /**
     * STS认证凭证使用 RAM 角色资源描述符（ARN）
     * 指定要扮演的 RAM 角色，用于获取临时安全凭证
     */
    private String ramRoleArn;

    /**
     * 临时凭证有效期（秒）
     * STS Token 的有效使用时间范围
     */
    private Long expire;

    /**
     * 服务端点
     */
    private String endpoint;
}
