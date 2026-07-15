package com.aliyun.core.alipay.transfer.domain;

import com.aliyun.core.alipay.transfer.enums.AlipayEreceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电子回单状态查询结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliPayEreceiptQueryResult {

    private boolean success;

    private String apiMethod;

    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private String fileId;

    private AlipayEreceiptStatus status;

    private String statusCode;

    private String downloadUrl;

    private String errorMessage;
}
