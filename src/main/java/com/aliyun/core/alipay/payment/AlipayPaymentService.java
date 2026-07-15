package com.aliyun.core.alipay.payment;

import com.aliyun.core.alipay.payment.domain.AliPayPaymentParam;
import com.aliyun.core.alipay.payment.domain.AliPayPaymentResult;
import com.aliyun.core.alipay.payment.domain.AliPayRefundParam;
import com.aliyun.core.alipay.payment.domain.AliPayRefundQueryParam;
import com.aliyun.core.alipay.payment.domain.AliPayRefundQueryResult;
import com.aliyun.core.alipay.payment.domain.AliPayRefundResult;
import com.aliyun.core.alipay.payment.domain.AliPayTradeCloseParam;
import com.aliyun.core.alipay.payment.domain.AliPayTradeCloseResult;
import com.aliyun.core.alipay.payment.domain.AliPayTradeQueryParam;
import com.aliyun.core.alipay.payment.domain.AliPayTradeQueryResult;

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

    /**
     * 关闭交易。
     *
     * @param closeParam 关闭参数
     * @return 关闭结果
     */
    AliPayTradeCloseResult close(AliPayTradeCloseParam closeParam);

    /**
     * 交易退款。
     *
     * @param refundParam 退款参数
     * @return 退款结果
     */
    AliPayRefundResult refund(AliPayRefundParam refundParam);

    /**
     * 退款查询。
     *
     * @param refundQueryParam 退款查询参数
     * @return 退款查询结果
     */
    AliPayRefundQueryResult queryRefund(AliPayRefundQueryParam refundQueryParam);

    /**
     * 交易查询。
     *
     * @param queryParam 交易查询参数
     * @return 交易查询结果
     */
    AliPayTradeQueryResult query(AliPayTradeQueryParam queryParam);
}
