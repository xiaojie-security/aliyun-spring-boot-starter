package com.aliyun.core.alipay.oauth2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网站应用授权链接请求参数实体类
 * 用于构建OAuth2.0授权请求链接，支持网页应用获取用户授权
 * 
 * @author YourName
 * @version 1.0
 * @since 2026-07-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorizationRequest {

    /**
     * 应用唯一标识（必填）
     * 在微信开放平台提交应用审核通过后获得
     */
    private String appid;
    
    /**
     * 回调地址（必填）
     * 用户授权后重定向的URL，需要使用URLEncode进行编码
     */
    private String redirectUri;

    
    /**
     * 应用授权作用域（必填）
     */
    private String scope = "auth_user";
    
    /**
     * 状态参数（可选）
     * 用于保持请求和回调的状态，授权请求后原样带回给第三方
     * 可用于防止CSRF攻击（跨站请求伪造攻击）
     * 建议设置为随机数加session进行校验
     */
    private String state;


    public AuthorizationRequest(String redirectUri, String state) {
        this.redirectUri = redirectUri;
        this.state = state;
    }
}
