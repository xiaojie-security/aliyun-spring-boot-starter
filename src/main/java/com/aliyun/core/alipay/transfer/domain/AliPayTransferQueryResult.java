package com.aliyun.core.alipay.transfer.domain;

import com.aliyun.core.alipay.transfer.enums.AlipayTransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账查询结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferQueryResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String arrivalTimeEnd;

    private String deductBillInfo;

    private String errorCode;

    private String failInstErrorCode;

    private String failInstName;

    private String failInstReason;

    private String failReason;

    private String inflowSettleSerialNo;

    private String orderFee;

    private String orderId;

    private String outBizNo;

    private String passbackParams;

    private String payDate;

    private String payFundOrderId;

    private String receiverOpenId;

    private String receiverUserId;

    private String settleSerialNo;

    private AlipayTransferStatus status;

    private String statusCode;

    private String subOrderErrorCode;

    private String subOrderFailReason;

    private String subOrderStatus;

    private String subStatus;

    private String transAmount;

    private String transferBillInfo;
}
