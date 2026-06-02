package com.aliyun.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AliyunMediaUploadDetails {

    /**
     * 文件存储桶名
     */
    private String bucket;

    /**
     * 文件存储路径
     */
    private String objectName;

    /**
     * 原始文件名
     */
    private String originFileName;

    /**
     * 最终处理后的文件名
     */
    private String finalFileName;

    /**
     * 文件 MD5 校验值
     */
    private String md5;

    /**
     * 文件内容类型（MIME 类型）
     */
    private String contentType;

    /**
     * 文件所属的 OSS 地域，用于存储和访问
     */
    private String region;

    /**
     * 文件访问的域名，用于生成文件访问 URL
     */
    private String endpoint;

    /**
     * 文件访问的 URI
     */
    private String uri;

}
