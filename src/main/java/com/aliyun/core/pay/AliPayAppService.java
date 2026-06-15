package com.aliyun.core.pay;

import com.alipay.api.*;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aliyun.core.exception.AliyunException;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliPayRefundParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 支付宝APP支付
 */
@Slf4j
@RequiredArgsConstructor
public class AliPayAppService extends AbstractAlipayService{
    /**
     * 支付宝 APP 支付固定产品码。
     */
    private static final String APP_PAY_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    private final com.alipay.api.AlipayClient client;
    private final AliPayDetails aliPayDetails;

    @Override
    protected AliPayDetails getAliPayDetails() {
        return aliPayDetails;
    }

    @Override
    protected AlipayClient getAlipayClient() {
        return client;
    }

    /**
     * 生成支付宝APP支付订单签名字符串
     * <p>
     * 根据订单信息生成支付宝APP支付所需的签名字符串（orderStr），客户端可使用该字符串调起支付宝支付界面。
     * </p>
     *
     * @param out_trade_no 商户订单号，需保证商户系统内唯一
     * @param total_amount 订单总金额，单位为元，精确到小数点后两位
     * @param subject 订单标题，商品名称或交易描述
     * @param isRoyaltyFreeze 是否分账冻结，true-冻结，false-不冻结
     * @return 支付宝支付签名字符串（orderStr），用于客户端调起支付
     * @throws AlipayApiException 支付宝API调用异常时抛出
     */
    public String generateOrderStr(String out_trade_no, BigDecimal total_amount, String subject, Boolean isRoyaltyFreeze, String notifyUrl) throws AlipayApiException {
        log.info("开始生成支付宝APP支付订单 - 商户订单号: {}, 订单金额: {}, 订单标题: {}, 是否分账冻结: {}",
                out_trade_no, total_amount, subject, isRoyaltyFreeze);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(notifyUrl);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setOutTradeNo(out_trade_no);
        model.setTotalAmount(total_amount.toPlainString());
        model.setSubject(subject);
        model.setProductCode(APP_PAY_PRODUCT_CODE);

        Long validityTime = aliPayDetails.getValidityTime();
        if (validityTime != null && validityTime > 0) {
            LocalDateTime expireTime = LocalDateTime.now().plusNanos(validityTime * 1_000_000);
            String timeExpire = expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            model.setTimeExpire(timeExpire);
            log.info("设置订单超时时间 - 商户订单号: {}, 超时时间: {}", out_trade_no, timeExpire);
        }


        model.setMerchantOrderNo(out_trade_no);

        request.setBizModel(model);

        try {
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            String orderStr = response.getBody();

            if (response.isSuccess()) {
                log.info("支付宝APP支付订单生成成功 - 商户订单号: {}, 签名字符串长度: {}", out_trade_no, orderStr != null ? orderStr.length() : 0);
                return orderStr;
            } else {
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.error("支付宝APP支付订单生成失败 - 商户订单号: {}, 错误码: {}, 错误消息: {}, 子错误码: {}, 子错误消息: {}, 诊断链接: {}",
                        out_trade_no, response.getCode(), response.getMsg(), response.getSubCode(), response.getSubMsg(), diagnosisUrl);
                throw AliPayException.REQUEST_PAY_ERROR;
            }
        } catch (AlipayApiException e) {
            log.error("支付宝APP支付订单生成异常 - 商户订单号: {}, 订单金额: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    out_trade_no, total_amount, e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw e;
        }
    }

    /**
     * 查询支付宝交易状态
     * <p>
     * 根据商户订单号或支付宝交易号查询交易的当前状态，包括交易是否成功、交易金额、买家信息等。
     * </p>
     *
     * @param out_trade_no 商户订单号，与 trade_no 不能同时为空
     * @param trade_no 支付宝交易号，与 out_trade_no 不能同时为空
     * @return 交易查询响应结果，包含交易状态、金额、时间等信息
     * @throws AliyunException 当两个查询参数同时为空时抛出
     */
    public AlipayTradeQueryResponse query(String out_trade_no, String trade_no) {
        if (out_trade_no == null && trade_no == null) {
            throw new AliyunException("商户订单号和支付宝交易号不能同时为空");
        }

        log.info("开始查询支付宝交易状态 - 商户订单号: {}, 支付宝交易号: {}", out_trade_no, trade_no);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        model.setOutTradeNo(out_trade_no);
        model.setTradeNo(trade_no);

        try {
            request.setBizModel(model);
            AlipayTradeQueryResponse response = execute(request);

            log.info("支付宝交易查询完成 - 商户订单号: {}, 支付宝交易号: {}, 响应码: {}, 响应消息: {}, 交易状态: {}",
                    out_trade_no, trade_no, response.getCode(), response.getMsg(), response.getTradeStatus());

            return response;
        } catch (AlipayApiException e) {
            log.error("支付宝交易查询失败 - 商户订单号: {}, 支付宝交易号: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    out_trade_no, trade_no, e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_ERROR;
        }
    }

    /**
     * 申请支付宝订单退款
     * <p>
     * 对已支付的订单发起退款申请，支持全额退款和部分退款。
     * </p>
     *
     * @param aliPayRefundParam 退款参数对象，包含：
     *                             - outTradeNo: 商户订单号
     *                             - tradeNo: 支付宝交易号
     *                             - refundAmount: 退款金额
     *                             - refundReason: 退款原因
     *                             - outRequestNo: 退款请求号（部分退款时必传）
     * @return 退款响应结果，包含退款金额、退款时间等信息
     */
    public AlipayTradeRefundResponse refund(AliPayRefundParam aliPayRefundParam) {
        log.info("开始申请支付宝订单退款 - 商户订单号: {}, 支付宝交易号: {}, 退款金额: {}, 退款原因: {}, 退款请求号: {}",
                aliPayRefundParam.getOutTradeNo(),
                aliPayRefundParam.getTradeNo(),
                aliPayRefundParam.getRefundAmount(),
                aliPayRefundParam.getRefundReason(),
                aliPayRefundParam.getOutRequestNo());

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        model.setOutTradeNo(aliPayRefundParam.getOutTradeNo());
        model.setTradeNo(aliPayRefundParam.getTradeNo());
        model.setRefundAmount(aliPayRefundParam.getRefundAmount().toPlainString());
        model.setRefundReason(aliPayRefundParam.getRefundReason());
        model.setOutRequestNo(aliPayRefundParam.getOutRequestNo());

        try {
            request.setBizModel(model);
            AlipayTradeRefundResponse response = execute(request);

            log.info("支付宝订单退款申请完成 - 商户订单号: {}, 支付宝交易号: {}, 响应码: {}, 响应消息: {}, 退款金额: {}, 资金变化: {} , 标题信息：{}",
                    aliPayRefundParam.getOutTradeNo(),
                    aliPayRefundParam.getTradeNo(),
                    response.getCode(),
                    response.getMsg(),
                    response.getRefundFee(),
                    response.getFundChange(),
                    response.getSubMsg());

            return response;
        } catch (AlipayApiException e) {
            log.error("支付宝订单退款失败 - 商户订单号: {}, 支付宝交易号: {}, 退款金额: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    aliPayRefundParam.getOutTradeNo(),
                    aliPayRefundParam.getTradeNo(),
                    aliPayRefundParam.getRefundAmount(),
                    e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REFUND_ERROR;
        }
    }

    /**
     * 查询支付宝订单退款结果
     * <p>
     * 根据商户订单号、支付宝交易号和退款请求号查询退款的处理结果。
     * </p>
     *
     * @param out_trade_no 商户订单号
     * @param trade_no 支付宝交易号
     * @param refund_request_no 退款请求号，标识一次退款请求
     * @return 退款查询响应结果，包含退款状态、退款金额、退款时间等信息
     */
    public AlipayTradeFastpayRefundQueryResponse queryRefund(String out_trade_no, String trade_no, String refund_request_no) {
        log.info("开始查询支付宝订单退款结果 - 商户订单号: {}, 支付宝交易号: {}, 退款请求号: {}",
                out_trade_no, trade_no, refund_request_no);

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();

        model.setTradeNo(trade_no);
        model.setOutTradeNo(out_trade_no);
        model.setOutRequestNo(refund_request_no);

        try {
            request.setBizModel(model);
            AlipayTradeFastpayRefundQueryResponse response = execute(request);

            log.info("支付宝订单退款查询完成 - 商户订单号: {}, 支付宝交易号: {}, 退款请求号: {}, 响应码: {}, 响应消息: {}, 退款状态: {}",
                    out_trade_no, trade_no, refund_request_no, response.getCode(), response.getMsg(), response.getRefundStatus());

            return response;
        } catch (AlipayApiException e) {
            log.error("查询支付宝订单退款结果失败 - 商户订单号: {}, 支付宝交易号: {}, 退款请求号: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    out_trade_no, trade_no, refund_request_no, e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.QUERY_REFUND_ERROR;
        }
    }


}
