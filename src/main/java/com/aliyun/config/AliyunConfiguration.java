package com.aliyun.config;

import com.aliyun.properties.AliyunProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云基础配置入口。
 * 仅负责启用配置属性绑定，具体客户端装配由各子配置类负责。
 */
@Configuration
@EnableConfigurationProperties(AliyunProperties.class)
public class AliyunConfiguration {
}
