package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资金账户查询参数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayFundAccountQueryParam {

    private String accountProductCode;

    private String accountSceneCode;

    private String accountType;

    private String alipayOpenId;

    private String alipayUserId;

    private String extInfo;

    private String merchantUserId;
}
