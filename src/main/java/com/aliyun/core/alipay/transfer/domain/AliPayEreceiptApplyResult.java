package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电子回单申请结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayEreceiptApplyResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String fileId;
}
