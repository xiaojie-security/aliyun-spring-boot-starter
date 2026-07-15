package com.aliyun.core.alipay.payment.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aliyun.core.alipay.AbstractAlipayService;
import com.aliyun.core.alipay.payment.AlipayPaymentService;
import com.aliyun.exception.AliPayException;
import com.aliyun.exception.AliyunException;
import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliPayRefundParam;
import com.aliyun.model.AliPayTradeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class DefaultAlipayPaymentService extends AbstractAlipayService implements AlipayPaymentService {
    private static final String APP_PAY_PRODUCT_CODE = "QUICK_MSECURITY_PAY";
    private static final String FUND_CHANGE = "Y";
    private static final String REFUND_SUCCESS = "REFUND_SUCCESS";

    private final AlipayClient client;
    private final AliPayDetails details;

    /**
     * 获取当前服务使用的支付宝配置。
     *
     * @return 支付宝配置
     */
    @Override
    protected AliPayDetails getAliPayDetails() {
        return details;
    }

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
     * 创建 APP 支付订单字符串。
     *
     * @param outTradeNo 商户订单号
     * @param totalAmount 支付金额
     * @param subject 订单标题
     * @param isRoyaltyFreeze 是否分账冻结
     * @param notifyUrl 支付回调地址
     * @return APP 拉起支付所需订单字符串
     * @throws AlipayApiException 支付宝接口异常
     */
    @Override
    public String appPay(String outTradeNo, BigDecimal totalAmount, String subject, Boolean isRoyaltyFreeze, String notifyUrl)
            throws AlipayApiException {
        log.info("DefaultAlipayPaymentService.appPay 开始生成支付宝APP支付订单 - 商户订单号: {}, 订单金额: {}, 订单标题: {}, 是否分账冻结: {}",
                outTradeNo, totalAmount, subject, isRoyaltyFreeze);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(notifyUrl);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(totalAmount.toPlainString());
        model.setSubject(subject);
        model.setProductCode(APP_PAY_PRODUCT_CODE);
        model.setMerchantOrderNo(outTradeNo);
        if (Boolean.TRUE.equals(isRoyaltyFreeze)) {
            log.info("DefaultAlipayPaymentService.appPay 支付宝APP支付请求启用了分账冻结标记，当前实现未单独下发该扩展字段 - 商户订单号: {}", outTradeNo);
        }

        Long validityTime = details.getValidityTime();
        if (validityTime != null && validityTime > 0) {
            LocalDateTime expireTime = LocalDateTime.now().plusNanos(validityTime * 1_000_000);
            String timeExpire = expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            model.setTimeExpire(timeExpire);
            log.info("DefaultAlipayPaymentService.appPay 设置支付宝APP支付订单超时时间 - 商户订单号: {}, 超时时间: {}", outTradeNo, timeExpire);
        }

        request.setBizModel(model);

        try {
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            if (response.isSuccess()) {
                return response.getBody();
            }
            log.error("DefaultAlipayPaymentService.appPay 支付宝APP支付订单生成失败 - 商户订单号: {}, 错误码: {}, 错误消息: {}, 子错误码: {}, 子错误消息: {}, 诊断链接: {}",
                    outTradeNo, response.getCode(), response.getMsg(), response.getSubCode(), response.getSubMsg(),
                    DiagnosisUtils.getDiagnosisUrl(response));
            throw AliPayException.REQUEST_PAY_ERROR;
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.appPay 支付宝APP支付订单生成异常 - 商户订单号: {}, 错误码: {}, 错误信息: {}",
                    outTradeNo, e.getErrCode(), e.getErrMsg(), e);
            throw e;
        }
    }

    /**
     * 创建网站支付表单。
     *
     * @param tradeParam 网站支付参数
     * @param notifyUrl 支付回调地址
     * @param returnUrl 支付返回地址
     * @return 网站支付表单 HTML
     * @throws AlipayApiException 支付宝接口异常
     */
    @Override
    public String pagePay(AliPayTradeParam tradeParam, String notifyUrl, String returnUrl) throws AlipayApiException {
        log.info("DefaultAlipayPaymentService.pagePay 开始生成支付宝网站支付订单 - 商户订单号: {}, 订单金额: {}, 订单标题: {}",
                tradeParam.getOutTradeNo(), tradeParam.getTotalAmount(), tradeParam.getSubject());

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(tradeParam.getOutTradeNo());
        model.setTotalAmount(tradeParam.getTotalAmount().toPlainString());
        model.setSubject(tradeParam.getSubject());
        model.setProductCode(tradeParam.getProductCode());
        model.setQrPayMode(tradeParam.getQrPayMode() == null ? null : String.valueOf(tradeParam.getQrPayMode()));
        model.setQrcodeWidth(tradeParam.getQrcodeWidth() == null ? null : tradeParam.getQrcodeWidth().longValue());
        model.setTimeExpire(tradeParam.getTimeExpire());
        model.setSubMerchant(tradeParam.getSubMerchant());
        model.setExtendParams(tradeParam.getExtendParams());
        model.setBusinessParams(tradeParam.getBusinessParams());
        model.setPromoParams(tradeParam.getPromoParams());
        model.setIntegrationType(tradeParam.getIntegrationType());
        model.setRequestFromUrl(tradeParam.getRequestFromUrl());
        model.setStoreId(tradeParam.getStoreId());
        model.setMerchantOrderNo(tradeParam.getMerchantOrderNo());
        model.setExtUserInfo(tradeParam.getExtUserInfo());
        GoodsDetail[] goodsDetails = tradeParam.getGoodsDetail();
        if (goodsDetails != null && goodsDetails.length > 0) {
            model.setGoodsDetail(Arrays.asList(goodsDetails));
        }
        request.setBizModel(model);

        try {
            AlipayTradePagePayResponse response = client.pageExecute(request, "POST");
            if (response.isSuccess()) {
                return response.getBody();
            }
            log.error("DefaultAlipayPaymentService.pagePay 支付宝网站支付订单生成失败 - 商户订单号: {}, 错误码: {}, 错误消息: {}, 子错误码: {}, 子错误消息: {}, 诊断链接: {}",
                    tradeParam.getOutTradeNo(), response.getCode(), response.getMsg(), response.getSubCode(),
                    response.getSubMsg(), DiagnosisUtils.getDiagnosisUrl(response));
            throw AliPayException.REQUEST_PAY_ERROR;
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.pagePay 支付宝网站支付订单生成异常 - 商户订单号: {}, 错误码: {}, 错误信息: {}",
                    tradeParam.getOutTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw e;
        }
    }

    /**
     * 查询交易。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @return 交易查询结果
     */
    @Override
    public AlipayTradeQueryResponse query(String outTradeNo, String tradeNo) {
        if (outTradeNo == null && tradeNo == null) {
            throw new AliyunException("商户订单号和支付宝交易号不能同时为空");
        }

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.query 支付宝交易查询失败 - 商户订单号: {}, 支付宝交易号: {}, 错误码: {}, 错误信息: {}",
                    outTradeNo, tradeNo, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_ERROR;
        }
    }

    /**
     * 关闭交易。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @return 交易关闭结果
     */
    @Override
    public AlipayTradeCloseResponse close(String outTradeNo, String tradeNo) {
        if (outTradeNo == null && tradeNo == null) {
            throw new AliyunException("商户订单号和支付宝交易号不能同时为空");
        }

        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.close 支付宝交易关闭失败 - 商户订单号: {}, 支付宝交易号: {}, 错误码: {}, 错误信息: {}",
                    outTradeNo, tradeNo, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_ERROR;
        }
    }

    /**
     * 发起退款。
     *
     * @param aliPayRefundParam 退款参数
     * @return 退款结果
     */
    @Override
    public AlipayTradeRefundResponse refund(AliPayRefundParam aliPayRefundParam) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        model.setOutTradeNo(aliPayRefundParam.getOutTradeNo());
        model.setTradeNo(aliPayRefundParam.getTradeNo());
        model.setRefundAmount(aliPayRefundParam.getRefundAmount().toPlainString());
        model.setRefundReason(aliPayRefundParam.getRefundReason());
        model.setOutRequestNo(aliPayRefundParam.getOutRequestNo());
        if (aliPayRefundParam.getRefundGoodsDetail() != null && aliPayRefundParam.getRefundGoodsDetail().length > 0) {
            model.setRefundGoodsDetail(Arrays.asList(aliPayRefundParam.getRefundGoodsDetail()));
        }
        if (aliPayRefundParam.getRefundRoyaltyParameters() != null
                && aliPayRefundParam.getRefundRoyaltyParameters().length > 0) {
            model.setRefundRoyaltyParameters(Arrays.asList(aliPayRefundParam.getRefundRoyaltyParameters()));
        }

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.refund 支付宝订单退款失败 - 商户订单号: {}, 支付宝交易号: {}, 错误码: {}, 错误信息: {}",
                    aliPayRefundParam.getOutTradeNo(), aliPayRefundParam.getTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REFUND_ERROR;
        }
    }

    /**
     * 查询退款。
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo 支付宝交易号
     * @param refundRequestNo 退款请求号
     * @return 退款查询结果
     */
    @Override
    public AlipayTradeFastpayRefundQueryResponse queryRefund(String outTradeNo, String tradeNo, String refundRequestNo) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setTradeNo(tradeNo);
        model.setOutTradeNo(outTradeNo);
        model.setOutRequestNo(refundRequestNo);

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.queryRefund 支付宝退款查询失败 - 商户订单号: {}, 支付宝交易号: {}, 退款请求号: {}, 错误码: {}, 错误信息: {}",
                    outTradeNo, tradeNo, refundRequestNo, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_REFUND_ERROR;
        }
    }

    /**
     * 判断退款调用是否成功。
     *
     * @param response 退款响应
     * @return true 表示退款成功
     */
    public static boolean isRefundSuccess(AlipayTradeRefundResponse response) {
        return response.isSuccess() && FUND_CHANGE.equals(response.getFundChange());
    }

    /**
     * 判断退款查询是否成功。
     *
     * @param response 退款查询响应
     * @return true 表示退款成功
     */
    public static boolean isRefundQuerySuccess(AlipayTradeFastpayRefundQueryResponse response) {
        return response.isSuccess() && REFUND_SUCCESS.equals(response.getRefundStatus());
    }
}
