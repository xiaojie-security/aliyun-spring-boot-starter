package com.aliyun.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AliyunSmsTemplateCode {

    /**
     * 登录/注册模板
     */
    LOGIN_REGISTER("login_register","100001",  "登录/注册模板"),

    /**
     * 修改绑定手机号模板
     */
    CHANGE_PHONE("change_phone","100002",  "修改绑定手机号模板"),

    /**
     * 密码重置模板
     */
    PASSWORD_RESET("password_reset","100003",  "密码重置模板"),

    /**
     * 绑定新手机号模板
     */
    BIND_PHONE("bind_phone","100004",  "绑定新手机号模板"),

    /**
     * 验证绑定手机号模板
     */
    VERIFY_BIND_PHONE("verify_bind_phone","100005",  "验证绑定手机号模板");

    /**
     * 模板类型
     */
    private final String type;

    /**
     * 模板编码
     */
    private final String templateCode;

    /**
     * 模板描述
     */
    private final String desc;

    /**
     * 根据模板类型获取枚举
     *
     * @param type 模板类型
     * @return 对应的枚举值，未找到返回 null
     */
    public static AliyunSmsTemplateCode of(String type) {
        if (StrUtil.isEmpty(type)) {
            throw new IllegalArgumentException("业务模板类型禁止为空");
        }
        for (AliyunSmsTemplateCode templateCode : values()) {
            if (templateCode.getType().equals(type)) {
                return templateCode;
            }
        }
        throw new IllegalArgumentException("非法业务模板类型");
    }


}
