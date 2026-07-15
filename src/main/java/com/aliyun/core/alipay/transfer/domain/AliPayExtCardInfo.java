package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外卡信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayExtCardInfo {

    private String bankAccName;

    private String cardBank;

    private String cardBranch;

    private String cardDeposit;

    private String cardLocation;

    private String cardNo;

    private String status;
}
