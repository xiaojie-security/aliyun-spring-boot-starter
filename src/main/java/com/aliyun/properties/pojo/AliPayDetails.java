package com.aliyun.properties.pojo;

import com.aliyun.properties.AliyunBaseProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 兼容旧版分组支付配置的明细对象。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliPayDetails extends AliyunBaseProperties {

    private String appId;

    private String gateWay;

    private String privateKey;

    private String publicKey;

    private String appCertPath;

    private String alipayPublicCertPath;

    private String rootCertPath;

    private String sellerId;

    private Long validityTime;

    private Boolean certificates;

    public boolean isCertificates() {
        return Boolean.TRUE.equals(certificates);
    }
}
