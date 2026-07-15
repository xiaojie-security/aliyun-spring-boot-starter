package com.aliyun.core.alipay.oauth2.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.aliyun.core.alipay.AbstractAlipayService;
import com.aliyun.core.alipay.oauth2.AliPayOAuth2Service;
import com.aliyun.core.alipay.oauth2.domain.AuthorizationRequest;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPaySystemOauthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class DefaultAliPayOAuth2Service extends AbstractAlipayService implements AliPayOAuth2Service {
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String AUTH_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    private final AlipayClient client;

    /**
     * 获取当前服务使用的支付宝客户端。
     *
     * @return 支付宝客户端
     */
    @Override
    protected AlipayClient getAlipayClient() {
        return client;
    }

    /**
     * 生成支付宝授权地址。
     *
     * @param request 授权请求参数
     * @return 授权地址
     */
    @Override
    public String generateAuthUrl(AuthorizationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("授权请求参数不能为空");
        }
        if (!StringUtils.hasText(request.getRedirectUri())) {
            throw new IllegalArgumentException("redirectUri 不能为空");
        }
        String appId = StringUtils.hasText(request.getAppid()) ? request.getAppid() : properties.getAppId();
        if (!StringUtils.hasText(appId)) {
            throw new IllegalArgumentException("appId 不能为空");
        }

        String scope = StringUtils.hasText(request.getScope()) ? request.getScope() : "auth_user";
        StringBuilder builder = new StringBuilder(AUTH_URL)
                .append("?app_id=").append(urlEncode(appId))
                .append("&scope=").append(urlEncode(scope))
                .append("&redirect_uri=").append(urlEncode(request.getRedirectUri()));

        if (StringUtils.hasText(request.getState())) {
            builder.append("&state=").append(urlEncode(request.getState()));
        }
        return builder.toString();
    }

    /**
     * 查询用户授权信息。
     *
     * @param accessToken 用户访问令牌
     * @return 用户授权信息
     */
    @Override
    public AlipayUserInfoShareResponse queryUserInfoShare(String accessToken) {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            return execute(request, accessToken);
        } catch (AlipayApiException e) {
            log.error("DefaultAliPayOAuth2Service.queryUserInfoShare 查询支付宝用户信息异常, accessToken={}, errCode={}, errMsg={}",
                    accessToken, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_USER_ERROR;
        }
    }

    /**
     * 通过授权码换取令牌。
     *
     * @param authorizationCode 授权码
     * @return OAuth2 令牌信息
     */
    @Override
    public AliPaySystemOauthDetails getAccessTokenByCode(String authorizationCode) {
        return getSystemOAuthToken(AUTHORIZATION_CODE, authorizationCode, null);
    }

    /**
     * 通过刷新令牌换取新令牌。
     *
     * @param refreshToken 刷新令牌
     * @return OAuth2 令牌信息
     */
    @Override
    public AliPaySystemOauthDetails refreshAccessToken(String refreshToken) {
        return getSystemOAuthToken(REFRESH_TOKEN, null, refreshToken);
    }

    /**
     * 统一获取 OAuth2 令牌。
     *
     * @param grantType 授权类型
     * @param code 授权码
     * @param refreshToken 刷新令牌
     * @return OAuth2 令牌信息
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
            log.error("DefaultAliPayOAuth2Service.getSystemOAuthToken 获取支付宝授权访问令牌异常, grantType={}, code={}, refreshToken={}, errCode={}, errMsg={}",
                    grantType, code, refreshToken, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_TOKEN_ERROR;
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
