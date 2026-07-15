package com.aliyun.core.alipay.payment;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aliyun.model.AliPayRefundParam;
import com.aliyun.model.AliPayTradeParam;

import java.math.BigDecimal;

public interface AlipayPaymentService {

    /**
     * 创建 APP 支付订单字符串。
     *
     * @param outTradeNo 商户订单号
     * @param totalAmount 支付金额
     * @param subject 订单标题
     * @param isRoyaltyFreeze 是否分账冻结
     * @param notifyUrl 支付通知地址
     * @return APP 拉起支付所需订单字符串
     * @throws AlipayApiException 支付宝接口异常
     */
    String appPay(String outTradeNo, BigDecimal totalAmount, String subject, Boolean isRoyaltyFreeze, String notifyUrl)
            throws AlipayApiException;

    /**
     * 创建网站支付表单。
     *
     * @param tradeParam 网站支付参数
     * @param notifyUrl 支付通知地址
     * @param returnUrl 支付完成跳转地址
     * @return 网站支付表单 HTML
     * @throws AlipayApiException 支付宝接口异常
     */
    String pagePay(AliPayTradeParam tradeParam, String notifyUrl, String returnUrl) throws AlipayApiException;

    /**
     * 查询交易。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @return 交易查询结果
     */
    AlipayTradeQueryResponse query(String outTradeNo, String tradeNo);

    /**
     * 关闭交易。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @return 交易关闭结果
     */
    AlipayTradeCloseResponse close(String outTradeNo, String tradeNo);

    /**
     * 发起退款。
     *
     * @param aliPayRefundParam 退款参数
     * @return 退款结果
     */
    AlipayTradeRefundResponse refund(AliPayRefundParam aliPayRefundParam);

    /**
     * 查询退款。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @param refundRequestNo 退款请求号
     * @return 退款查询结果
     */
    AlipayTradeFastpayRefundQueryResponse queryRefund(String outTradeNo, String tradeNo, String refundRequestNo);
}
