package com.aliyun.core.pay;

import cn.hutool.core.bean.BeanUtil;
import com.alipay.v3.ApiException;
import com.alipay.v3.api.AlipayTradeApi;
import com.alipay.v3.api.AlipayTradeFastpayRefundApi;
import com.alipay.v3.model.*;
import com.alipay.v3.util.GenericExecuteApi;
import com.aliyun.exception.AliyunException;
import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliPayRefundParam;
import com.aliyun.model.AliPayTradeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云扫码支付服务 ScanCode
 */
@Slf4j
@RequiredArgsConstructor
public class AliPayScanCodeService {
    private final com.alipay.v3.ApiClient payClient;
    public static final String ALIPAY_TRADE_PAGE_PAY = "alipay.trade.page.pay";

    public String payment(String out_trade_no, BigDecimal total_amount, String subject, GoodsDetail[] goodsDetails) {
        GenericExecuteApi api = new GenericExecuteApi(payClient);
        // 构造请求参数以调用接口
        Map<String, Object> bizParams = new HashMap<>();
        AliPayTradeParam param = new AliPayTradeParam(out_trade_no, total_amount, subject);
        param.setGoodsDetail(goodsDetails);
        Map<String, Object> bizContent = BeanUtil.beanToMap(param);
        bizParams.put("biz_content", bizContent);
        try {
            return api.pageExecute(ALIPAY_TRADE_PAGE_PAY, "POST", bizParams);
        } catch (ApiException e) {
            log.error("AliPayScanCodeService.payment 统一下单并支付异常：{}", e.getMessage());
            throw new AliyunException(e.getMessage());
        }
    }

    public AlipayTradeQueryResponseModel query(String out_trade_no, String trade_no) {
        if (out_trade_no == null && trade_no == null) {
            throw new AliyunException("商户订单号和支付宝交易号不能同时为空");
        }
        // 构造请求参数以调用接口
        AlipayTradeApi api = new AlipayTradeApi(payClient);
        AlipayTradeQueryModel data = new AlipayTradeQueryModel();
        // 设置订单支付时传入的商户订单号
        data.setOutTradeNo(out_trade_no);
        // 设置支付宝交易号
        data.setTradeNo(trade_no);
        try {
            return api.query(data);
        } catch (ApiException e) {
            AlipayTradeQueryDefaultResponse errorObject = (AlipayTradeQueryDefaultResponse) e.getErrorObject();
            log.error("AliPayScanCodeService.query 交易查询失败 订单号：{} 失败原因：{}",out_trade_no,errorObject.getAlipayTradeQueryErrorResponseModel().getMessage());
            return null;
        }
    }

    public AlipayTradeCloseResponseModel close(String out_trade_no, String trade_no) {
        // 构造请求参数以调用接口
        AlipayTradeApi api = new AlipayTradeApi(payClient);
        AlipayTradeCloseModel data = new AlipayTradeCloseModel();
        // 设置该交易在支付宝系统中的交易流水号
        data.setTradeNo(trade_no);
        // 设置订单支付时传入的商户订单号
        data.setOutTradeNo(out_trade_no);
        try {
            return api.close(data);
        } catch (ApiException e) {
            AlipayTradeCloseDefaultResponse errorObject = (AlipayTradeCloseDefaultResponse) e.getErrorObject();
            log.error("AliPayScanCodeService.close 关闭订单失败 订单号：{} 失败原因：{}",out_trade_no,errorObject.getAlipayTradeCloseErrorResponseModel().getMessage());
            return null;
        }
    }

    public AlipayTradeRefundResponseModel refund(AliPayRefundParam aliPayRefundParam) {
        // 构造请求参数以调用接口
        AlipayTradeApi api = new AlipayTradeApi(payClient);
        AlipayTradeRefundModel data = new AlipayTradeRefundModel();
        // 设置商户订单号
        data.setOutTradeNo(aliPayRefundParam.getOutTradeNo());
        // 设置支付宝交易号
        data.setTradeNo(aliPayRefundParam.getTradeNo());
        // 设置退款金额
        data.setRefundAmount(aliPayRefundParam.getRefundAmount().toPlainString());
        // 设置退款原因说明
        data.setRefundReason(aliPayRefundParam.getRefundReason());
        // 设置退款请求号
        data.setOutRequestNo(aliPayRefundParam.getOutRequestNo());

        try {
            return api.refund(data);
        } catch (ApiException e) {
            AlipayTradeRefundDefaultResponse errorObject = (AlipayTradeRefundDefaultResponse) e.getErrorObject();
            log.error("AliPayScanCodeService.refund 订单退款失败 订单号：{} 失败原因：{}", aliPayRefundParam.getOutTradeNo(),errorObject.getAlipayTradeRefundErrorResponseModel().getMessage());
            return null;
        }
    }

    public AlipayTradeFastpayRefundQueryResponseModel queryRefund(String out_request_no, String out_trade_no, String trade_no) {
        // 构造请求参数以调用接口
        AlipayTradeFastpayRefundApi api = new AlipayTradeFastpayRefundApi(payClient);
        AlipayTradeFastpayRefundQueryModel data = new AlipayTradeFastpayRefundQueryModel();
        // 设置支付宝交易号
        data.setTradeNo(trade_no);
        // 设置商户订单号
        data.setOutTradeNo(out_trade_no);
        // 设置退款请求号
        data.setOutRequestNo(out_request_no);
        try {
            return api.query(data);
        } catch (ApiException e) {
            AlipayTradeFastpayRefundQueryDefaultResponse errorObject = (AlipayTradeFastpayRefundQueryDefaultResponse) e.getErrorObject();
            log.error("AliPayScanCodeService.queryRefund 退款查询失败 订单号：{} 失败原因：{}",out_request_no,errorObject.getAlipayTradeFastpayRefundQueryErrorResponseModel().getMessage());
            return null;
        }
    }
}
