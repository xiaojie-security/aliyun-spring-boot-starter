package com.aliyun.core.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import com.aliyun.core.exception.AliyunException;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPayDetails;
import com.aliyun.model.AliPayRefundParam;
import com.aliyun.model.AliPaySystemOauthDetails;
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
public class AppAliPayService {
    /**
     * 支付宝 APP 支付固定产品码。
     */
    private static final String APP_PAY_PRODUCT_CODE = "QUICK_MSECURITY_PAY";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final com.alipay.api.AlipayClient appPayClient;
    private final AliPayDetails aliPayDetails;


    /**
     * 查询支付宝用户授权信息
     * <p>
     * 使用访问令牌获取用户的授权基本信息，包括用户ID、昵称、头像等。
     * 该接口需要在用户授权后调用，且访问令牌需具备相应的用户信息读取权限。
     * </p>
     *
     * @param accessToken 用户授权访问令牌，通过OAuth授权流程获取
     * @return 用户信息共享响应，包含用户基本信息
     * @throws AliPayException 当支付宝API调用失败时抛出异常
     */
    public AlipayUserInfoShareResponse queryUserInfoShare(String accessToken) {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            return appPayClient.certificateExecute(request,accessToken);
        } catch (AlipayApiException e) {
            log.error("查询用户信息异常 - 错误码: {}, 错误信息: {}", e.getErrCode(), e.getErrMsg());
            throw AliPayException.QUERY_USER_ERROR;
        }
    }

    /**
     * 通过授权码获取系统OAuth令牌
     * <p>
     * 使用支付宝授权码换取访问令牌和刷新令牌，用于后续API调用。
     * </p>
     *
     * @param authorizationCode 支付宝授权码，由用户授权后获得
     * @return OAuth令牌详情，包含访问令牌、刷新令牌和用户OpenID
     */
    public AliPaySystemOauthDetails querySystemOAuthTokenByAuthorizationCode(String authorizationCode) {
        return getSystemOAuthToken(AUTHORIZATION_CODE, authorizationCode, null);
    }

    /**
     * 通过刷新令牌获取系统OAuth令牌
     * <p>
     * 使用刷新令牌重新获取访问令牌，适用于访问令牌过期的场景。
     * </p>
     *
     * @param refreshToken 刷新令牌，用于获取新的访问令牌
     * @return OAuth令牌详情，包含新的访问令牌、刷新令牌和用户OpenID
     */
    public AliPaySystemOauthDetails querySystemOAuthTokenByRefreshToken(String refreshToken) {
        return getSystemOAuthToken(REFRESH_TOKEN, null, refreshToken);
    }

    /**
     * 获取支付宝系统OAuth访问令牌
     * <p>
     * 根据授权类型（授权码或刷新令牌）调用支付宝接口获取访问令牌。
     * 支持两种授权方式：
     * <ul>
     *     <li>authorization_code - 使用授权码换取令牌</li>
     *     <li>refresh_token - 使用刷新令牌更新令牌</li>
     * </ul>
     * </p>
     *
     * @param grantType 授权类型，可选值：AUTHORIZATION_CODE 或 REFRESH_TOKEN
     * @param code 授权码，当 grantType 为 AUTHORIZATION_CODE 时必填
     * @param refreshToken 刷新令牌，当 grantType 为 REFRESH_TOKEN 时必填
     * @return OAuth令牌详情，包含访问令牌、刷新令牌和用户OpenID
     * @throws AliPayException 当支付宝API调用失败时抛出异常
     */
    private AliPaySystemOauthDetails getSystemOAuthToken(String grantType, String code, String refreshToken) {
        // 构造请求参数以调用接口
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();

        // 设置刷新令牌
        request.setRefreshToken(refreshToken);

        // 设置授权码
        request.setCode(code);

        // 设置授权方式
        request.setGrantType(grantType);

        try {
            AlipaySystemOauthTokenResponse response = appPayClient.certificateExecute(request);
            String aToken = response.getAccessToken();
            String rToken = response.getRefreshToken();
            String openId = response.getOpenId();
            return AliPaySystemOauthDetails.builder()
                    .accessToken(aToken)
                    .refreshToken(rToken)
                    .openId(openId)
                    .build();
        } catch (AlipayApiException e) {
            log.error("获取授权访问令牌异常 - 错误码: {}, 错误信息: {}", e.getErrCode(), e.getErrMsg(),e);
            throw AliPayException.REQUEST_TOKEN_ERROR;
        }
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
    public String generateOrderStr(String out_trade_no, BigDecimal total_amount, String subject, Boolean isRoyaltyFreeze, String notify_url) throws AlipayApiException {
        log.info("开始生成支付宝APP支付订单 - 商户订单号: {}, 订单金额: {}, 订单标题: {}, 是否分账冻结: {}",
                out_trade_no, total_amount, subject, isRoyaltyFreeze);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(notify_url);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setOutTradeNo(out_trade_no);
        model.setTotalAmount(total_amount.toPlainString());
        model.setSubject(subject);
        // APP 支付场景必须传入固定产品码，否则客户端可能提示商家订单参数异常。
        model.setProductCode(APP_PAY_PRODUCT_CODE);
//        model.setGoodsDetail(goodsDetails);

        Long validityTime = aliPayDetails.getValidityTime();
        if (validityTime != null && validityTime > 0) {
            LocalDateTime expireTime = LocalDateTime.now().plusNanos(validityTime * 1_000_000);
            String timeExpire = expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            model.setTimeExpire(timeExpire);
            log.info("设置订单超时时间 - 商户订单号: {}, 超时时间: {}", out_trade_no, timeExpire);
        }

//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setRoyaltyFreeze(isRoyaltyFreeze.toString());
//        extendParams.setCardType("S0JP0000");
//        model.setExtendParams(extendParams);

        model.setMerchantOrderNo(out_trade_no);

//        List<String> queryOptions = new ArrayList<String>();
//        queryOptions.add("hyb_amount");
//        queryOptions.add("enterprise_pay_info");
//        model.setQueryOptions(queryOptions);

        request.setBizModel(model);

        try {
            AlipayTradeAppPayResponse response = appPayClient.sdkExecute(request);
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
            AlipayTradeQueryResponse response = appPayClient.certificateExecute(request);

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
            AlipayTradeRefundResponse response = appPayClient.certificateExecute(request);

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
            AlipayTradeFastpayRefundQueryResponse response = appPayClient.certificateExecute(request);

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
