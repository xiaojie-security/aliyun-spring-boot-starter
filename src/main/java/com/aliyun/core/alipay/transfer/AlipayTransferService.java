package com.aliyun.core.alipay.transfer;

import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;

public interface AlipayTransferService {

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
    AlipayFundTransUniTransferResponse transfer(String outBizNo, String transAmount, String orderTitle, String openId, String remark);

    /**
     * 查询转账结果。
     *
     * @param outBizNo 商户转账单号
     * @param orderId 支付宝转账单据号
     * @param payFundOrderId 支付宝支付资金流水号
     * @return 转账查询结果
     */
    AlipayFundTransCommonQueryResponse query(String outBizNo, String orderId, String payFundOrderId);
}
