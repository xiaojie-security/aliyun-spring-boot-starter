package com.aliyun.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AliPaySystemOauthDetails {

    /**
     * 认证令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * openId
     */
    private String openId;
}
