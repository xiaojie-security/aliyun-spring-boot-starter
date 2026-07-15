package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账额度查询结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayFundQuotaQueryResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private Boolean activeNewQuotaDailyRemainLimited;

    private String activeNewQuotaDailyRemainLimitType;

    private Boolean activeNewQuotaMonthlyRemainLimited;

    private String activeNewQuotaMonthlyRemainLimitType;

    private Boolean activeQuotaIsNew;

    private String newQuotaDaily;

    private String newQuotaDailyRemain;

    private String newQuotaMonthly;

    private String newQuotaMonthlyRemain;

    private String newQuotaSingleMax;

    private String newQuotaSingleMin;

    private String toCorporateDailyAvailableAmount;

    private String toCorporateMonthlyAvailableAmount;

    private String toPrivateDailyAvailableAmount;

    private String toPrivateMonthlyAvailableAmount;
}
