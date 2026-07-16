package com.aliyun.properties;

import cn.hutool.core.collection.CollUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 阿里云短信服务配置类
 *
 * <p>用于封装阿里云短信服务的相关配置信息，包括签名、接入地址和模板编码等</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "aliyun.sms")
@Slf4j
public class AliyunSmsProperties extends AliyunBaseProperties implements InitializingBean {

    /**
     * 短信服务接入端点
     */
    private String endpoint;

    /**
     * 短信服务接入区域
     */
    private String region;

    /**
     * 短信签名映射
     */
    private Map<String, String> signNames;

    /**
     * 默认签名
     */
    private String defaultSignName;

    /**
     * STS认证凭证使用 RAM 角色资源描述符（ARN）
     * 指定要扮演的 RAM 角色，用于获取临时安全凭证
     */
    private String ramRoleArn;

    public String getSignName() {
        return defaultSignName;
    }

    public String getSignName(String signKey) {
        if (CollUtil.isEmpty(signNames)) {
            return defaultSignName;
        }
        return signNames.getOrDefault(signKey, defaultSignName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isEnable()) {
            log.debug("AliyunSmsProperties.afterPropertiesSet SMS短信服务");
        }
    }

}


