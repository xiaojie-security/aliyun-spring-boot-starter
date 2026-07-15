package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 单笔转账参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferParam {

    private String bizScene;

    private String businessParams;

    private AliPayTransferMultiCurrencyDetail mutipleCurrencyDetail;

    private String orderTitle;

    private String originalOrderId;

    private String outBizNo;

    private String passbackParams;

    private AliPayTransferParticipant payeeInfo;

    private AliPayTransferParticipant payerInfo;

    private String productCode;

    private String remark;

    private AliPayTransferSignData signData;

    private BigDecimal transAmount;

    private String transferSceneName;

    private AliPayTransferSceneReportInfo[] transferSceneReportInfos;

    public AliPayTransferParam(String outBizNo, BigDecimal transAmount, String orderTitle, AliPayTransferParticipant payeeInfo) {
        this.outBizNo = outBizNo;
        this.transAmount = transAmount;
        this.orderTitle = orderTitle;
        this.payeeInfo = payeeInfo;
    }
}
