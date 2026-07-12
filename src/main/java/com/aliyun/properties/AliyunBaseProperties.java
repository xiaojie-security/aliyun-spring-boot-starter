package com.aliyun.properties;

import lombok.Data;

@Data
public class AliyunBaseProperties {

    /**
     * 是否开启服务
     */
    protected Boolean enable;

    /**
     * 阿里云访问密钥 ID
     * 用于标识用户的 AccessKey 身份
     */
    protected String accessKeyId;

    /**
     * 阿里云访问密钥 Secret
     * 用于签名验证的密钥信息
     */
    protected String accessKeySecret;

    /**
     * 是否开启服务。
     *
     * @return true-开启，false-未开启
     */
    protected boolean isEnable() {
        return Boolean.TRUE.equals(enable);
    }
}
