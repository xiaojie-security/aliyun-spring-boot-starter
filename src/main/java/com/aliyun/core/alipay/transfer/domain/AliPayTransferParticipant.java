package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账参与方信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferParticipant {

    private AliPayBankcardExtInfo bankcardExtInfo;

    private String certNo;

    private String certType;

    private String extInfo;

    private String identity;

    private String identityType;

    private String merchantUserInfo;

    private String name;
}
