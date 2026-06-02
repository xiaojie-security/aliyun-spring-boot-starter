package com.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 阿里云认证信息封装类
 * 用于存储和管理临时安全凭证，包括 STS Token 及其相关认证信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliyunStsSecurityCredential {

    /**
     * 安全令牌（Security Token）
     * 临时访问凭证的核心部分，用于身份验证
     */
    private String securityToken;

    /**
     * 访问密钥 Secret
     * 用于请求签名的密钥信息
     */
    private String accessKeySecret;

    /**
     * 访问密钥 ID
     * 用于标识用户身份的 AccessKey 标识符
     */
    private String accessKeyId;

    /**
     * 凭证过期时间
     * 临时凭证的有效期截止时间
     */
    private String expiration;

}

