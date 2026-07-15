package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账单据查询参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferQueryParam {

    private String bizScene;

    private String orderId;

    private String outBizNo;

    private String payFundOrderId;

    private String productCode;
}
