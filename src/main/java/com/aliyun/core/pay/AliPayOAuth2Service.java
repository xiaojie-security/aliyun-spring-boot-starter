package com.aliyun.core.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliPaySystemOauthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝OAuth2服务。
 */
@Slf4j
@RequiredArgsConstructor
public class AliPayOAuth2Service extends AbstractAlipayService{
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final com.alipay.api.AlipayClient client;
    private final AliPayDetails details;

    @Override
    protected AliPayDetails getAliPayDetails() {
        return details;
    }

    @Override
    protected AlipayClient getAlipayClient() {
        return client;
    }

    /**
     * 查询支付宝用户授权信息。
     *
     * @param accessToken 用户授权访问令牌
     * @return 用户授权信息
     */
    public AlipayUserInfoShareResponse queryUserInfoShare(String accessToken) {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            return execute(request, accessToken);
        } catch (AlipayApiException e) {
            log.error("AliPayOAuth2Service.queryUserInfoShare 查询用户信息异常, accessToken={}, errCode={}, errMsg={}",
                    accessToken, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_USER_ERROR;
        }
    }

    /**
     * 通过授权码获取系统OAuth令牌。
     *
     * @param authorizationCode 支付宝授权码
     * @return OAuth令牌详情
     */
    public AliPaySystemOauthDetails querySystemOAuthTokenByAuthorizationCode(String authorizationCode) {
        return getSystemOAuthToken(AUTHORIZATION_CODE, authorizationCode, null);
    }

    /**
     * 通过刷新令牌获取系统OAuth令牌。
     *
     * @param refreshToken 刷新令牌
     * @return OAuth令牌详情
     */
    public AliPaySystemOauthDetails querySystemOAuthTokenByRefreshToken(String refreshToken) {
        return getSystemOAuthToken(REFRESH_TOKEN, null, refreshToken);
    }

    /**
     * 获取支付宝系统OAuth访问令牌。
     *
     * @param grantType 授权类型
     * @param code 授权码
     * @param refreshToken 刷新令牌
     * @return OAuth令牌详情
     */
    private AliPaySystemOauthDetails getSystemOAuthToken(String grantType, String code, String refreshToken) {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setRefreshToken(refreshToken);
        request.setCode(code);
        request.setGrantType(grantType);

        try {
            AlipaySystemOauthTokenResponse response = execute(request);
            return AliPaySystemOauthDetails.builder()
                    .accessToken(response.getAccessToken())
                    .refreshToken(response.getRefreshToken())
                    .openId(response.getOpenId())
                    .build();
        } catch (AlipayApiException e) {
            log.error("AliPayOAuth2Service.getSystemOAuthToken 获取授权访问令牌异常, grantType={}, code={}, refreshToken={}, errCode={}, errMsg={}",
                    grantType, code, refreshToken, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_TOKEN_ERROR;
        }
    }

}
