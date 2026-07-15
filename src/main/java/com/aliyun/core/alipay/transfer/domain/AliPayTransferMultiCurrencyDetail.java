package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 多币种转账信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferMultiCurrencyDetail {

    private String extInfo;

    private String paymentAmount;

    private String paymentCurrency;

    private String settlementAmount;

    private String settlementCurrency;

    private String transAmount;

    private String transCurrency;
}
