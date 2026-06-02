package com.aliyun.utils;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public final class AlipayUtils {

    public static final String FUND_CHANGE = "Y";
    public static final String REFUND_SUCCESS = "REFUND_SUCCESS";
    public static final String SUCCESS = "SUCCESS";
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";


    /**
     * 判断支付宝退款是否成功
     * <p>
     * 根据支付宝退款响应结果，判断退款操作是否成功执行。
     * 需要同时满足两个条件：
     * 1. 响应状态为成功（isSuccess = true）
     * 2. 资金发生变化（fundChange = "Y"）
     * </p>
     *
     * @param response 支付宝退款响应对象，包含退款结果和资金变化信息
     * @return true-退款成功且资金已变化；false-退款失败或资金未变化
     */
    public static boolean isRefundSuccess(AlipayTradeRefundResponse response) {
        return response.isSuccess() && FUND_CHANGE.equals(response.getFundChange());
    }

    /**
     * 判断支付宝退款查询结果是否成功
     * <p>
     * 根据支付宝退款查询响应结果，判断退款处理状态是否为成功。
     * 需要同时满足两个条件：
     * 1. 响应状态为成功（isSuccess = true）
     * 2. 退款状态为成功（refundStatus = "REFUND_SUCCESS"）
     * </p>
     *
     * @param response 支付宝退款查询响应对象，包含退款处理状态信息
     * @return true-退款处理成功；false-退款处理中、失败或查询失败
     */
    public static boolean isRefundQuerySuccess(AlipayTradeFastpayRefundQueryResponse response) {
        return response.isSuccess() && REFUND_SUCCESS.equals(response.getRefundStatus());
    }

    /**
     * 判断支付宝单笔转账是否成功。
     * <p>
     * 根据支付宝转账响应结果，判断转账操作是否成功执行。
     * 需要同时满足两个条件：
     * 1. 响应状态为成功（isSuccess = true）
     * 2. 转账状态为成功（status = "SUCCESS"）
     * </p>
     *
     * @param response 支付宝转账响应对象，包含转账结果和状态信息
     * @return true-转账成功；false-转账失败或响应异常
     */
    public static boolean isTransferSuccess(AlipayFundTransUniTransferResponse response) {
        return response.isSuccess() && SUCCESS.equals(response.getStatus());
    }

    /**
     * 判断支付宝转账查询结果是否成功。
     * <p>
     * 根据支付宝转账查询响应结果，判断转账处理状态是否为成功。
     * 需要同时满足两个条件：
     * 1. 响应状态为成功（isSuccess = true）
     * 2. 转账状态为成功（status = "SUCCESS"）
     * </p>
     *
     * @param response 支付宝转账查询响应对象，包含转账处理状态信息
     * @return true-转账处理成功；false-转账处理中、失败或查询失败
     */
    public static boolean isTransferSuccess(AlipayFundTransCommonQueryResponse response) {
        return response.isSuccess() && SUCCESS.equals(response.getStatus());
    }


    /**
     * 验证支付宝异步回调签名并提取参数。
     * <p>
     * 从HTTP请求中提取支付宝异步通知参数，并使用支付宝公钥证书验证签名合法性。
     * 处理流程：
     * 1. 解析请求参数，将多值参数用逗号拼接
     * 2. 使用RSA证书模式验证签名
     * 3. 验证失败或异常时返回null
     * </p>
     *
     * @param request HTTP请求对象，包含支付宝异步通知参数
     * @param alipayPublicCertPath 支付宝公钥证书路径
     * @return 验证成功返回参数字典；验证失败或异常返回null
     */
    public static Map<String, String> rsaCertCheck(HttpServletRequest request, String alipayPublicCertPath)  {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
//            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        try {
            boolean signVerified = AlipaySignature.rsaCertCheckV1(params, alipayPublicCertPath, CHARSET, SIGN_TYPE);  //调用SDK验证签名
            if (!signVerified){
                log.error("AlipayUtils rsaCertCheck 验证支付宝异步回调签名失败");
                return null;
            }
            return params;
        } catch (AlipayApiException e) {
            log.error("Alipay signature verification failed", e);
            return null;
        }
    }

}
