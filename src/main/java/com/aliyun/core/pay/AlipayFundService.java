package com.aliyun.core.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.aliyun.exception.AliPayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝资金账户
 */
@Slf4j
@RequiredArgsConstructor
public class AlipayFundService {
    public static final String ALIPAY_OPEN_ID = "ALIPAY_OPEN_ID";
    private final String PRODUCT_CODE = "TRANS_ACCOUNT_NO_PWD";
    private final String BIZ_SCENE = "DIRECT_TRANSFER";
    private final com.alipay.api.AlipayClient fundPayClient;


    /**
     * 单笔转账
     *
     * @param outBizNo 商户订单号
     * @param transAmount 转账金额
     * @param orderTitle 订单标题
     * @param openId 收款openId
     * @param remark 备注
     * @return 转账响应结果
     */
    public AlipayFundTransUniTransferResponse transfer(String outBizNo, String transAmount, String orderTitle, String openId, String remark) {
        log.info("开始执行支付宝单笔转账 - 商户订单号: {}, 转账金额: {}, 订单标题: {}, 收款方openId: {}, 备注: {}",
                outBizNo, transAmount, orderTitle, openId, remark);

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
            AlipayFundTransUniTransferResponse response = fundPayClient.certificateExecute(request);

            log.info("支付宝单笔转账完成 - 商户订单号: {}, 响应码: {}, 响应消息: {}, 转账状态: {}",
                    outBizNo, response.getCode(), response.getMsg(), response.getStatus());

            return response;
        } catch (AlipayApiException e) {
            log.error("支付宝单笔转账失败 - 商户订单号: {}, 转账金额: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    outBizNo, transAmount, e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.TRANSFER_ERROR;
        }
    }

    /**
     * 主动查询转账结果
     *
     * @param outBizNo 商户转账唯一订单号
     * @param orderId 支付宝转账单据号
     * @param payFundOrderId 支付宝支付资金流水号
     * @return 查询响应结果
     */
    public AlipayFundTransCommonQueryResponse query(String outBizNo, String orderId, String payFundOrderId) {
        log.info("开始查询支付宝转账结果 - 商户订单号: {}, 支付宝转账单据号: {}, 支付宝支付资金流水号: {}",
                outBizNo, orderId, payFundOrderId);

        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();

        model.setProductCode(PRODUCT_CODE);
        model.setBizScene(BIZ_SCENE);
        model.setOutBizNo(outBizNo);
        model.setOrderId(orderId);
        model.setPayFundOrderId(payFundOrderId);

        try {
            request.setBizModel(model);
            AlipayFundTransCommonQueryResponse response = fundPayClient.execute(request);

            log.info("支付宝转账查询完成 - 商户订单号: {}, 支付宝转账单据号: {}, 响应码: {}, 响应消息: {}, 转账状态: {}",
                    outBizNo, orderId, response.getCode(), response.getMsg(), response.getStatus());

            return response;
        } catch (AlipayApiException e) {
            log.error("支付宝转账查询失败 - 商户订单号: {}, 支付宝转账单据号: {}, 支付宝支付资金流水号: {}, 错误码: {}, 错误信息: {}, 子码: {}, 子信息: {}",
                    outBizNo, orderId, payFundOrderId, e.getErrCode(), e.getErrMsg(), e.getErrCode(), e.getErrMsg(), e);
            throw AliPayException.TRANSFER_ERROR;
        }
    }

}
