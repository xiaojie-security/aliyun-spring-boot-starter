package com.aliyun.core.oss;



import com.aliyun.model.AliyunStsSecurityCredential;
import com.aliyun.model.AliyunMediaUploadDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.codehaus.jettison.json.JSONException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface AliyunOssService {


    /**
     * 上传媒体文件并返回媒体详情信息
     *
     * @param originalFilename 原始文件名
     * @param inputStream    文件输入流
     * @return 上传后的媒体详情对象
     */
    AliyunMediaUploadDetails upload(String originalFilename, InputStream inputStream);

    /**
     * 使用分片上传文件并返回媒体详情信息
     *
     * @param originFileName 原始文件名
     * @param inputStream    文件输入流
     * @return 上传后的媒体详情对象
     */
    AliyunMediaUploadDetails multipartUpload(String originFileName, InputStream inputStream);


    /**
     * 从指定的存储桶下载对象到本地文件系统
     *
     * @param bucket     存储桶名称，指定要从中下载对象的存储桶
     * @param objectName 对象名称，指定要下载的具体对象标识符
     * @param path       本地文件路径，指定对象下载后保存的本地文件路径
     */
    boolean download(String bucket, String objectName, String path);

    /**
     * 删除指定存储桶中的单个对象
     *
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     */
    boolean delete(String bucket, String objectName);

    /**
     * 批量删除指定存储桶中的多个对象
     *
     * @param bucket      存储桶名称
     * @param objectNames 对象名称列表
     */
    boolean delete(String bucket, List<String> objectNames);


    /**
     * 检查指定存储桶中的对象是否存在
     *
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     * @return 如果对象存在返回true，否则返回false
     */
    boolean checkExist(String bucket, String objectName);


    /**
     * 生成临时签名 URL 用于访问私有 OSS 资源
     * 当存储桶为私有时，通过签名 URL 提供临时的访问权限
     * @param bucket OSS 存储桶名称
     * @param objectName 文件在 OSS 中的完整路径（不包含桶名）
     * @return 签名后的临时访问 URL
     */
    String generatePresignedUrl(String bucket, String objectName);


    /**
     * 生成POST签名，用于上传文件
     * @return 签名信息
     */
    Map<String, String> generatePostSignatureForOssUpload(String accessKeyId,String accessKeySecret, String securityToken) throws JsonProcessingException, JSONException;


    /**
     * 视频单帧截取。
     *
     * <p>基于阿里云 OSS 视频截帧能力，为指定视频对象生成单帧截图访问地址。</p>
     *
     * @param bucket OSS 存储桶名称
     * @param objectName 视频对象名称
     * @param timeInMillis 截帧时间点，单位毫秒
     * @return 视频单帧截图的签名访问地址
     */
    String generateVideoSnapshotPresignedUrl(String bucket, String objectName, Long timeInMillis);

}
