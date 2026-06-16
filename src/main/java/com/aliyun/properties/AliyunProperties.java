package com.aliyun.properties;


import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliyunCredential;
import com.aliyun.properties.pojo.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 阿里云服务配置属性类
 * 用于加载和管理 application.yml/yml 中配置的阿里云相关参数
 * 支持通过前缀 "aliyun" 自动绑定配置文件中的属性值
 */
@ConfigurationProperties(prefix = "aliyun")
@Data
@Slf4j
public class AliyunProperties implements InitializingBean {

    /**
     * OSS 对象存储服务配置
     * 包含存储桶、认证信息和转码配置等
     */
    private AliyunOss oss;

    /**
     * IMM 转码配置
     * 用于配置 OSS 转码服务
     */
    private AliyunImm imm;

    /**
     * 支付服务配置
     * 用于配置支付宝支付服务
     */
    private AliPay pay;

    /**
     * 短信服务配置
     * 用于配置阿里云短信服务
     */
    private AliyunSms sms;

    /**
     * 号码认证服务配置
     * 用于配置阿里云号码认证服务
     */
    private AliyunPns pns;

    /**
     * sts服务配置，用于临时凭证访问
     * 用于配置阿里云临时凭证服务
     */
    private AliyunSts sts;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("========== 阿里云服务配置状态 ==========");

        if (sts != null) {
            log.info("STS 临时凭证访问服务: {}", sts.isEnable() ? "已开启" : "已关闭");
        }


        if (oss != null) {
            log.info("OSS 对象存储服务: {}", oss.isEnable() ? "已开启" : "已关闭");
        }

        if (imm != null) {
            log.info("IMM 智能媒体服务: {}", imm.isEnable() ? "已开启" : "已关闭");
        }

        if (pay != null) {
            AliPayDetails scanCode = pay.getScanCode();
            AliPayDetails fund = pay.getFund();
            AliPayDetails app = pay.getApp();
            AliPayDetails oauth = pay.getOauth();
            log.info("PAY 支付宝扫码支付服务: {}", scanCode.isEnable() ? "已开启" : "已关闭");
            log.info("PAY 支付宝转账服务: {}", fund.isEnable() ? "已开启" : "已关闭");
            log.info("PAY 支付宝APP支付服务: {}", app.isEnable() ? "已开启" : "已关闭");
            log.info("PAY 支付宝OAuth服务: {}", oauth.isEnable() ? "已开启" : "已关闭");
        }

        if (sms != null) {
            log.info("SMS 短信服务: {}", sms.isEnable() ? "已开启" : "已关闭");
        }

        if (pns != null) {
            log.info("PNS 号码认证服务: {}", pns.isEnable() ? "已开启" : "已关闭");
        }

        log.info("========================================");
    }

    public AliyunCredential createStsCredential(String ramRoleArn) {
        if (sts == null) {
            log.info("STS 临时凭证服务: {}", "未开启");
            return null;
        }
        if (!sts.isEnable()) {
            log.info("STS 临时凭证服务: {}", "已关闭");
            return null;
        }
        return new AliyunCredential(sts.getAccessKeyId(), sts.getAccessKeySecret(), ramRoleArn, sts.getExpire(), sts.getEndpoint());
    }

}
