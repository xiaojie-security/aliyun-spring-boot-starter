package com.aliyun.properties.pojo;

import lombok.Data;

@Data
public class AliyunBaseProperties {

    /**
     * 是否开启服务
     * */
    private boolean enable = false;

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
}

