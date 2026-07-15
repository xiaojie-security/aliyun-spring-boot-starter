package com.aliyun.core.alipay.payment;

import com.aliyun.model.AliPayPaymentParam;
import com.aliyun.model.AliPayPaymentResult;

public interface AlipayPaymentService {

    /**
     * 统一收单线下交易预创建。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    AliPayPaymentResult precreate(AliPayPaymentParam paymentParam);

    /**
     * APP 支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    AliPayPaymentResult appPay(AliPayPaymentParam paymentParam);

    /**
     * 手机网站支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    AliPayPaymentResult wapPay(AliPayPaymentParam paymentParam);

    /**
     * PC 端支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    AliPayPaymentResult pagePay(AliPayPaymentParam paymentParam);
}
