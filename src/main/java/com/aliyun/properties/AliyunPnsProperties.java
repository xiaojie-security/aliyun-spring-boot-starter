package com.aliyun.properties;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云号码认证服务配置类
 *
 * <p>用于封装阿里云短信服务的相关配置信息，包括签名、接入地址和模板编码等</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "aliyun.pns")
@Slf4j
public class AliyunPnsProperties extends AliyunBaseProperties implements InitializingBean {

    /**
     * 签名
     */
    private String signName;

    /**
     * 号码认证服务接入端点
     */
    private String endpoint;

    /**
     * 号码认证服务接入区域
     */
    private String region;

    /**
     * STS认证凭证使用 RAM 角色资源描述符（ARN）
     * 指定要扮演的 RAM 角色，用于获取临时安全凭证
     */
    private String ramRoleArn;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isEnable()) {
            log.debug("AliyunPnsProperties.afterPropertiesSet PNS号码认证");
        }
    }

}


