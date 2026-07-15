package com.aliyun.core.alipay.payment.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.aliyun.core.alipay.AbstractAlipayService;
import com.aliyun.core.alipay.payment.AlipayPaymentService;
import com.aliyun.enums.AlipayPaymentType;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPayPaymentParam;
import com.aliyun.model.AliPayPaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DefaultAlipayPaymentService extends AbstractAlipayService implements AlipayPaymentService {
    private static final String APP_PRODUCT_CODE = "QUICK_MSECURITY_PAY";
    private static final String PAGE_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";
    private static final String PRECREATE_PRODUCT_CODE = "FACE_TO_FACE_PAYMENT";
    private static final String WAP_PRODUCT_CODE = "QUICK_WAP_WAY";
    private static final String PAGE_EXECUTE_METHOD = "POST";
    private static final String DEFAULT_INTEGRATION_TYPE = "PCWEB";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final com.alipay.api.AlipayClient alipayClient;

    @Override
    protected AlipayClient getAlipayClient() {
        return alipayClient;
    }

    /**
     * 统一收单线下交易预创建。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    @Override
    public AliPayPaymentResult precreate(AliPayPaymentParam paymentParam) {
        validatePaymentParam(paymentParam);

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        model.setOutTradeNo(paymentParam.getOutTradeNo());
        model.setTotalAmount(formatAmount(paymentParam.getTotalAmount()));
        model.setSubject(paymentParam.getSubject());
        model.setBody(paymentParam.getBody());
        model.setProductCode(resolveProductCode(paymentParam.getProductCode(), PRECREATE_PRODUCT_CODE));
        model.setTimeExpire(resolveTimeExpire(paymentParam));
        model.setTimeoutExpress(paymentParam.getTimeoutExpress());
        model.setSellerId(resolveSellerId(paymentParam.getSellerId()));
        model.setStoreId(paymentParam.getStoreId());
        model.setMerchantOrderNo(paymentParam.getMerchantOrderNo());
        model.setPassbackParams(paymentParam.getPassbackParams());
        model.setEnablePayChannels(paymentParam.getEnablePayChannels());
        model.setDisablePayChannels(paymentParam.getDisablePayChannels());
        model.setBuyerLogonId(paymentParam.getBuyerLogonId());
        model.setCodeType(paymentParam.getCodeType());
        model.setOperatorId(paymentParam.getOperatorId());
        model.setTerminalId(paymentParam.getTerminalId());
        model.setPaymentType(paymentParam.getPaymentType());
        model.setDiscountableAmount(formatNullableAmount(paymentParam.getDiscountableAmount()));
        model.setUndiscountableAmount(formatNullableAmount(paymentParam.getUndiscountableAmount()));
        model.setGoodsDetail(toList(paymentParam.getGoodsDetail()));
        model.setQueryOptions(toList(paymentParam.getQueryOptions()));
        model.setExtUserInfo(paymentParam.getExtUserInfo());
        model.setExtendParams(paymentParam.getExtendParams());
        model.setSubMerchant(paymentParam.getSubMerchant());
        model.setSettleInfo(paymentParam.getSettleInfo());
        model.setRoyaltyInfo(paymentParam.getRoyaltyInfo());

        request.setNotifyUrl(paymentParam.getNotifyUrl());
        request.setBizModel(model);

        try {
            AlipayTradePrecreateResponse response = execute(request);
            return AliPayPaymentResult.builder()
                    .success(response.isSuccess())
                    .paymentType(AlipayPaymentType.PRECREATE)
                    .apiMethod(AlipayPaymentType.PRECREATE.getApiMethod())
                    .code(response.getCode())
                    .msg(response.getMsg())
                    .subCode(response.getSubCode())
                    .subMsg(response.getSubMsg())
                    .outTradeNo(response.getOutTradeNo())
                    .sellerId(resolveSellerId(paymentParam.getSellerId()))
                    .totalAmount(formatAmount(paymentParam.getTotalAmount()))
                    .payData(response.getQrCode())
                    .qrCode(response.getQrCode())
                    .prepayId(response.getPrepayId())
                    .shareCode(response.getShareCode())
                    .build();
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.precreate 支付宝预创建订单失败, outTradeNo={}, errCode={}, errMsg={}",
                    paymentParam.getOutTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_PAY_ERROR;
        }
    }

    /**
     * APP 支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    @Override
    public AliPayPaymentResult appPay(AliPayPaymentParam paymentParam) {
        validatePaymentParam(paymentParam);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setOutTradeNo(paymentParam.getOutTradeNo());
        model.setTotalAmount(formatAmount(paymentParam.getTotalAmount()));
        model.setSubject(paymentParam.getSubject());
        model.setBody(paymentParam.getBody());
        model.setProductCode(resolveProductCode(paymentParam.getProductCode(), APP_PRODUCT_CODE));
        model.setTimeExpire(resolveTimeExpire(paymentParam));
        model.setTimeoutExpress(paymentParam.getTimeoutExpress());
        model.setSellerId(resolveSellerId(paymentParam.getSellerId()));
        model.setStoreId(paymentParam.getStoreId());
        model.setMerchantOrderNo(paymentParam.getMerchantOrderNo());
        model.setPassbackParams(paymentParam.getPassbackParams());
        model.setBusinessParams(paymentParam.getBusinessParams());
        model.setPromoParams(paymentParam.getPromoParams());
        model.setGoodsType(paymentParam.getGoodsType());
        model.setEnablePayChannels(paymentParam.getEnablePayChannels());
        model.setDisablePayChannels(paymentParam.getDisablePayChannels());
        model.setSpecifiedChannel(paymentParam.getSpecifiedChannel());
        model.setGoodsDetail(toList(paymentParam.getGoodsDetail()));
        model.setQueryOptions(toList(paymentParam.getQueryOptions()));
        model.setExtUserInfo(paymentParam.getExtUserInfo());
        model.setExtendParams(paymentParam.getExtendParams());
        model.setSubMerchant(paymentParam.getSubMerchant());
        model.setSettleInfo(paymentParam.getSettleInfo());
        model.setRoyaltyInfo(paymentParam.getRoyaltyInfo());

        request.setNotifyUrl(paymentParam.getNotifyUrl());
        request.setReturnUrl(paymentParam.getReturnUrl());
        request.setBizModel(model);

        try {
            AlipayTradeAppPayResponse response = getAlipayClient().sdkExecute(request);
            return buildSdkOrPageResult(response, paymentParam, AlipayPaymentType.APP);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.appPay 支付宝 APP 支付下单失败, outTradeNo={}, errCode={}, errMsg={}",
                    paymentParam.getOutTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_PAY_ERROR;
        }
    }

    /**
     * 手机网站支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    @Override
    public AliPayPaymentResult wapPay(AliPayPaymentParam paymentParam) {
        validatePaymentParam(paymentParam);

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

        model.setOutTradeNo(paymentParam.getOutTradeNo());
        model.setTotalAmount(formatAmount(paymentParam.getTotalAmount()));
        model.setSubject(paymentParam.getSubject());
        model.setBody(paymentParam.getBody());
        model.setProductCode(resolveProductCode(paymentParam.getProductCode(), WAP_PRODUCT_CODE));
        model.setTimeExpire(resolveTimeExpire(paymentParam));
        model.setTimeoutExpress(paymentParam.getTimeoutExpress());
        model.setSellerId(resolveSellerId(paymentParam.getSellerId()));
        model.setStoreId(paymentParam.getStoreId());
        model.setMerchantOrderNo(paymentParam.getMerchantOrderNo());
        model.setPassbackParams(paymentParam.getPassbackParams());
        model.setBusinessParams(paymentParam.getBusinessParams());
        model.setPromoParams(paymentParam.getPromoParams());
        model.setGoodsType(paymentParam.getGoodsType());
        model.setEnablePayChannels(paymentParam.getEnablePayChannels());
        model.setDisablePayChannels(paymentParam.getDisablePayChannels());
        model.setSpecifiedChannel(paymentParam.getSpecifiedChannel());
        model.setAuthToken(paymentParam.getAuthToken());
        model.setQuitUrl(paymentParam.getQuitUrl());
        model.setGoodsDetail(toList(paymentParam.getGoodsDetail()));
        model.setQueryOptions(toList(paymentParam.getQueryOptions()));
        model.setExtUserInfo(paymentParam.getExtUserInfo());
        model.setExtendParams(paymentParam.getExtendParams());
        model.setSubMerchant(paymentParam.getSubMerchant());
        model.setSettleInfo(paymentParam.getSettleInfo());
        model.setRoyaltyInfo(paymentParam.getRoyaltyInfo());

        request.setNotifyUrl(paymentParam.getNotifyUrl());
        request.setReturnUrl(paymentParam.getReturnUrl());
        request.setBizModel(model);

        try {
            AlipayTradeWapPayResponse response = getAlipayClient().pageExecute(request, PAGE_EXECUTE_METHOD);
            return buildSdkOrPageResult(response, paymentParam, AlipayPaymentType.WAP);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.wapPay 支付宝 H5 支付下单失败, outTradeNo={}, errCode={}, errMsg={}",
                    paymentParam.getOutTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_PAY_ERROR;
        }
    }

    /**
     * PC 端支付。
     *
     * @param paymentParam 支付参数
     * @return 统一支付结果
     */
    @Override
    public AliPayPaymentResult pagePay(AliPayPaymentParam paymentParam) {
        validatePaymentParam(paymentParam);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        model.setOutTradeNo(paymentParam.getOutTradeNo());
        model.setTotalAmount(formatAmount(paymentParam.getTotalAmount()));
        model.setSubject(paymentParam.getSubject());
        model.setBody(paymentParam.getBody());
        model.setProductCode(resolveProductCode(paymentParam.getProductCode(), PAGE_PRODUCT_CODE));
        model.setTimeExpire(resolveTimeExpire(paymentParam));
        model.setTimeoutExpress(paymentParam.getTimeoutExpress());
        model.setStoreId(paymentParam.getStoreId());
        model.setMerchantOrderNo(paymentParam.getMerchantOrderNo());
        model.setPassbackParams(paymentParam.getPassbackParams());
        model.setBusinessParams(paymentParam.getBusinessParams());
        model.setPromoParams(paymentParam.getPromoParams());
        model.setGoodsType(paymentParam.getGoodsType());
        model.setEnablePayChannels(paymentParam.getEnablePayChannels());
        model.setDisablePayChannels(paymentParam.getDisablePayChannels());
        model.setIntegrationType(resolveIntegrationType(paymentParam.getIntegrationType()));
        model.setQrPayMode(paymentParam.getQrPayMode() == null ? null : String.valueOf(paymentParam.getQrPayMode()));
        model.setQrcodeWidth(paymentParam.getQrcodeWidth());
        model.setRequestFromUrl(paymentParam.getRequestFromUrl());
        model.setGoodsDetail(toList(paymentParam.getGoodsDetail()));
        model.setQueryOptions(toList(paymentParam.getQueryOptions()));
        model.setExtUserInfo(paymentParam.getExtUserInfo());
        model.setExtendParams(paymentParam.getExtendParams());
        model.setSubMerchant(paymentParam.getSubMerchant());
        model.setSettleInfo(paymentParam.getSettleInfo());
        model.setRoyaltyInfo(paymentParam.getRoyaltyInfo());

        request.setNotifyUrl(paymentParam.getNotifyUrl());
        request.setReturnUrl(paymentParam.getReturnUrl());
        request.setBizModel(model);

        try {
            AlipayTradePagePayResponse response = getAlipayClient().pageExecute(request, PAGE_EXECUTE_METHOD);
            return buildSdkOrPageResult(response, paymentParam, AlipayPaymentType.PAGE);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayPaymentService.pagePay 支付宝 PC 支付下单失败, outTradeNo={}, errCode={}, errMsg={}",
                    paymentParam.getOutTradeNo(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.REQUEST_PAY_ERROR;
        }
    }

    private AliPayPaymentResult buildSdkOrPageResult(AlipayResponse response, AliPayPaymentParam paymentParam,
                                                     AlipayPaymentType paymentType) {
        return AliPayPaymentResult.builder()
                .success(response.isSuccess())
                .paymentType(paymentType)
                .apiMethod(paymentType.getApiMethod())
                .code(response.getCode())
                .msg(response.getMsg())
                .subCode(response.getSubCode())
                .subMsg(response.getSubMsg())
                .outTradeNo(paymentParam.getOutTradeNo())
                .sellerId(resolveSellerId(paymentParam.getSellerId()))
                .totalAmount(formatAmount(paymentParam.getTotalAmount()))
                .payData(response.getBody())
                .build();
    }

    private void validatePaymentParam(AliPayPaymentParam paymentParam) {
        if (paymentParam == null) {
            throw new IllegalArgumentException("支付参数不能为空");
        }
        if (!StringUtils.hasText(paymentParam.getOutTradeNo())) {
            throw new IllegalArgumentException("商户订单号不能为空");
        }
        if (paymentParam.getTotalAmount() == null || paymentParam.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("订单总金额必须大于0");
        }
        if (!StringUtils.hasText(paymentParam.getSubject())) {
            throw new IllegalArgumentException("订单标题不能为空");
        }
    }

    private String resolveProductCode(String productCode, String defaultProductCode) {
        return StringUtils.hasText(productCode) ? productCode : defaultProductCode;
    }

    private String resolveSellerId(String sellerId) {
        if (StringUtils.hasText(sellerId)) {
            return sellerId;
        }
        return properties == null ? null : properties.getSellerId();
    }

    private String resolveIntegrationType(String integrationType) {
        return StringUtils.hasText(integrationType) ? integrationType : DEFAULT_INTEGRATION_TYPE;
    }

    private String resolveTimeExpire(AliPayPaymentParam paymentParam) {
        if (StringUtils.hasText(paymentParam.getTimeExpire())) {
            return paymentParam.getTimeExpire();
        }
        if (properties == null || properties.getValidityTime() == null || properties.getValidityTime() <= 0) {
            return null;
        }
        return LocalDateTime.now().plus(properties.getValidityTime(), ChronoUnit.MILLIS)
                .format(TIME_FORMATTER);
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatNullableAmount(BigDecimal amount) {
        return amount == null ? null : formatAmount(amount);
    }

    private <T> List<T> toList(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return Arrays.asList(array);
    }
}
