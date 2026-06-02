package com.aliyun.properties.pojo;

import cn.hutool.core.util.StrUtil;
import lombok.*;

import java.util.List;
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
public class AliyunSms extends AliyunBaseProperties {

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
    private List<String> signNames;

    /**
     * 默认签名
     */
    private String defaultSignName;

    /**
     * 模板编码列表
     */
    private Map<String,String> templateCodes;

    /**
     * STS认证凭证使用 RAM 角色资源描述符（ARN）
     * 指定要扮演的 RAM 角色，用于获取临时安全凭证
     */
    private String ramRoleArn;

    /**
     * 获取模板编码
     * @param templateCodeKey 模板编码的 key
     * @return 模板编码
     */
    public String getTemplateCode(String templateCodeKey) {
        String code = templateCodes.get(templateCodeKey);
        if (StrUtil.isEmpty(code)) {
            throw new RuntimeException("模板编码不存在");
        }
        return code;
    }


}


