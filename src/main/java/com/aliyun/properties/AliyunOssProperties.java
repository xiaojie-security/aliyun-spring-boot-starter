package com.aliyun.properties;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 阿里云 OSS 对象存储配置类
 * 用于封装 OSS 服务的连接信息、认证配置和存储桶设置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "aliyun.oss")
@Slf4j
public class AliyunOssProperties extends AliyunBaseProperties implements InitializingBean {

    /**
     * OSS 服务接入点地址
     * 指定 OSS 服务的地域节点 Endpoint
     */
    private String endpoint;

    /**
     * OSS 服务访问 URI
     * 访问 OSS 服务的 URI
     */
    private String uri;

    /**
     * OSS 服务的区域
     * 存储桶所在的区域
     */
    private String region;

    /**
     * OSS 存储桶映射配置
     * key 为业务标识，value 为实际的 Bucket 名称
     */
    private Map<String,String> buckets;

    /**
     * 默认存储桶名称
     * 当未指定具体 Bucket 时使用的默认存储桶
     */
    private String defaultBucket;

    /**
     * 临时凭证有效期（秒）
     * 临时凭证的有效使用时间范围
     */
    private Long expire;

    /**
     * STS认证凭证使用 RAM 角色资源描述符（ARN）
     * 指定要扮演的 RAM 角色，用于获取临时安全凭证
     */
    private String ramRoleArn;

    /**
     * 设置上传回调URL（即回调服务器地址），必须为公网地址。用于处理应用服务器与OSS之间的通信，OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
     */
    private String callback;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (isEnable()) {
            log.debug("AliyunOssProperties.afterPropertiesSet OSS对象存储");
        }
    }
}

