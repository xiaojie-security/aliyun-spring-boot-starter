package com.aliyun.model;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;

/**
 * 号码认证服务的请求参数实体
 */
@Data
public class AliyunPnsTemplateParam {

    /**
     * 验证码
     */
    private String code;

    /**
     * 有效时间
     */
    private String min;

    private AliyunPnsTemplateParam(String code, String min) {
        this.code = code;
        this.min = min;
    }

    private AliyunPnsTemplateParam() {
        // 阿里云动态生成
        this.code = "##code##";
        this.min = "5";
    }

    public static AliyunPnsTemplateParam create(String code, String min) {
        return new AliyunPnsTemplateParam(code, min);
    }

    public static AliyunPnsTemplateParam create(String code) {
        return new AliyunPnsTemplateParam(code, "5");
    }

    public static AliyunPnsTemplateParam create() {
        return new AliyunPnsTemplateParam();
    }
}

