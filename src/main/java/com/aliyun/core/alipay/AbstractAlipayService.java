package com.aliyun.core.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.aliyun.properties.AlipayProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractAlipayService {

    protected abstract com.alipay.api.AlipayClient getAlipayClient();

    @Resource
    protected AlipayProperties properties;

    /**
     * 执行支付宝请求。
     *
     * @param request 支付宝请求
     * @param <T>     响应类型
     * @return 支付宝响应
     * @throws AlipayApiException 支付宝接口异常
     */
    protected <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
        if (useCertificateMode()) {
            return getAlipayClient().certificateExecute(request);
        }
        return getAlipayClient().execute(request);
    }

    /**
     * 执行带访问令牌的支付宝请求。
     *
     * @param request     支付宝请求
     * @param accessToken 访问令牌
     * @param <T>         响应类型
     * @return 支付宝响应
     * @throws AlipayApiException 支付宝接口异常
     */
    protected <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken) throws AlipayApiException {
        if (useCertificateMode()) {
            return getAlipayClient().certificateExecute(request, accessToken);
        }
        return getAlipayClient().execute(request, accessToken);
    }

    /**
     * 是否启用证书模式。
     *
     * @return true-启用证书模式，false-公钥模式
     */
    protected boolean useCertificateMode() {
        return properties != null && properties.isCertificates();
    }
}
