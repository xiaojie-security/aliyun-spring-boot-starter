package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特殊转账签名信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferSignData {

    private String oriAppId;

    private String oriCharSet;

    private String oriOutBizNo;

    private String oriSign;

    private String oriSignType;

    private String partnerId;
}
