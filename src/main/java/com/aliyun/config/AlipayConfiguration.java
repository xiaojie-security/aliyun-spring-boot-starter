package com.aliyun.config;

import com.aliyun.properties.AlipayProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 支付宝基础配置入口。
 * 仅负责启用配置属性绑定，具体客户端装配由各子配置类负责。
 */
@AutoConfiguration
public class AlipayConfiguration {

    protected static final String FORMAT = "json";
    protected static final String CHARSET = "UTF-8";
    protected static final String SIGN_TYPE = "RSA2";

    /**
     * 注册支付宝全局配置 Bean。
     *
     * @return 支付宝全局配置
     */
    @Bean
    public AlipayProperties aliPayProperties(){
        return new AlipayProperties();
    }


    @Bean
    public com.alipay.api.AlipayConfig alipayConfig(AlipayProperties aliPayProperties){
        com.alipay.api.AlipayConfig alipayConfig = new com.alipay.api.AlipayConfig();
        alipayConfig.setServerUrl(aliPayProperties.getGateWay());
        alipayConfig.setAppId(aliPayProperties.getAppId());
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setPrivateKey(aliPayProperties.getPrivateKey());
        if (aliPayProperties.isCertificates()) {
            alipayConfig.setAppCertPath(aliPayProperties.getAppCertPath());
            alipayConfig.setAlipayPublicCertPath(aliPayProperties.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(aliPayProperties.getRootCertPath());
        }
        alipayConfig.setAlipayPublicKey(aliPayProperties.getPublicKey());
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return alipayConfig;
    }


}
