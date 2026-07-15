package com.aliyun.core.alipay.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转账场景上报信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayTransferSceneReportInfo {

    private String infoContent;

    private String infoType;
}
