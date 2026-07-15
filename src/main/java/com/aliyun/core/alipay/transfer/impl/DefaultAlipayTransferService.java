package com.aliyun.core.alipay.transfer.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.aliyun.core.alipay.AbstractAlipayService;
import com.aliyun.core.alipay.transfer.AlipayTransferService;
import com.aliyun.exception.AliPayException;
import com.aliyun.model.AliPayDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultAlipayTransferService extends AbstractAlipayService implements AlipayTransferService {
    public static final String ALIPAY_OPEN_ID = "ALIPAY_OPEN_ID";
    private static final String PRODUCT_CODE = "TRANS_ACCOUNT_NO_PWD";
    private static final String BIZ_SCENE = "DIRECT_TRANSFER";
    private static final String SUCCESS = "SUCCESS";

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
     * 发起单笔转账。
     *
     * @param outBizNo 商户转账单号
     * @param transAmount 转账金额
     * @param orderTitle 转账标题
     * @param openId 收款方 openId
     * @param remark 转账备注
     * @return 转账结果
     */
    @Override
    public AlipayFundTransUniTransferResponse transfer(String outBizNo, String transAmount, String orderTitle, String openId, String remark) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

        model.setOutBizNo(outBizNo);
        model.setTransAmount(transAmount);
        model.setBizScene(BIZ_SCENE);
        model.setProductCode(PRODUCT_CODE);
        model.setOrderTitle(orderTitle);

        Participant participant = new Participant();
        participant.setIdentity(openId);
        participant.setIdentityType(ALIPAY_OPEN_ID);

        model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
        model.setPayeeInfo(participant);
        model.setRemark(remark);

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayTransferService.transfer 支付宝单笔转账失败 - 商户订单号: {}, 转账金额: {}, 错误码: {}, 错误信息: {}",
                    outBizNo, transAmount, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.TRANSFER_ERROR;
        }
    }

    /**
     * 查询转账结果。
     *
     * @param outBizNo 商户转账单号
     * @param orderId 支付宝转账单据号
     * @param payFundOrderId 支付宝支付资金流水号
     * @return 转账查询结果
     */
    @Override
    public AlipayFundTransCommonQueryResponse query(String outBizNo, String orderId, String payFundOrderId) {
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();

        model.setProductCode(PRODUCT_CODE);
        model.setBizScene(BIZ_SCENE);
        model.setOutBizNo(outBizNo);
        model.setOrderId(orderId);
        model.setPayFundOrderId(payFundOrderId);

        try {
            request.setBizModel(model);
            return execute(request);
        } catch (AlipayApiException e) {
            log.error("DefaultAlipayTransferService.query 支付宝转账查询失败 - 商户订单号: {}, 支付宝转账单据号: {}, 支付宝支付资金流水号: {}, 错误码: {}, 错误信息: {}",
                    outBizNo, orderId, payFundOrderId, e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.TRANSFER_ERROR;
        }
    }

    /**
     * 判断单笔转账是否成功。
     *
     * @param response 转账响应
     * @return true 表示转账成功
     */
    public static boolean isTransferSuccess(AlipayFundTransUniTransferResponse response) {
        return response.isSuccess() && SUCCESS.equals(response.getStatus());
    }

    /**
     * 判断转账查询是否成功。
     *
     * @param response 转账查询响应
     * @return true 表示转账成功
     */
    public static boolean isTransferSuccess(AlipayFundTransCommonQueryResponse response) {
        return response.isSuccess() && SUCCESS.equals(response.getStatus());
    }
}
