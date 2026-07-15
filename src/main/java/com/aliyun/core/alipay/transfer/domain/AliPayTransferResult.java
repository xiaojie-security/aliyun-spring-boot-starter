package com.aliyun.core.alipay.transfer.domain;

import com.aliyun.core.alipay.transfer.enums.AlipayTransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单笔转账结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String amount;

    private String link;

    private String orderId;

    private String outBizNo;

    private String payFundOrderId;

    private String settleSerialNo;

    private AlipayTransferStatus status;

    private String statusCode;

    private String subStatus;

    private String transDate;
}
