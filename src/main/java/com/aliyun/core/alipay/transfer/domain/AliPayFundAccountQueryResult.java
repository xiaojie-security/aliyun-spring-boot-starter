package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资金账户查询结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayFundAccountQueryResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String totalAmount;

    private String availableAmount;

    private String freezeAmount;

    private AliPayBalanceAccountDetail amountDetail;

    private AliPayExtCardInfo extCardInfo;
}
