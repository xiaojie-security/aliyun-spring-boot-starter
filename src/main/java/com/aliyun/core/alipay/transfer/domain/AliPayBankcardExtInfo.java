package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 银行卡扩展信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayBankcardExtInfo {

    private String accountType;

    private String bankCode;

    private String instBranchName;

    private String instCity;

    private String instName;

    private String instProvince;
}
