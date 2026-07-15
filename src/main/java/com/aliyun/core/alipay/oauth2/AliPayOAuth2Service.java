package com.aliyun.core.alipay.oauth2;

import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.aliyun.model.AliPaySystemOauthDetails;

public interface AliPayOAuth2Service {

    /**
     * 查询用户授权信息。
     *
     * @param accessToken 用户访问令牌
     * @return 用户授权信息
     */
    AlipayUserInfoShareResponse queryUserInfoShare(String accessToken);

    /**
     * 通过授权码换取令牌。
     *
     * @param authorizationCode 授权码
     * @return OAuth2 令牌信息
     */
    AliPaySystemOauthDetails querySystemOAuthTokenByAuthorizationCode(String authorizationCode);

    /**
     * 通过刷新令牌换取新令牌。
     *
     * @param refreshToken 刷新令牌
     * @return OAuth2 令牌信息
     */
    AliPaySystemOauthDetails querySystemOAuthTokenByRefreshToken(String refreshToken);
}
