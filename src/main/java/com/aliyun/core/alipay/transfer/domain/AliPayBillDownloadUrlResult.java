package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账单下载地址查询结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayBillDownloadUrlResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String billDownloadUrl;

    private String billFileCode;
}
