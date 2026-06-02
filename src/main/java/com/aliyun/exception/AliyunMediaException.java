package com.aliyun.exception;

public class AliyunMediaException extends RuntimeException {

    public AliyunMediaException() {
    }

    public AliyunMediaException(String message) {
        super(message);
    }

    public final static AliyunMediaException SYSTEM_ERROR = new AliyunMediaException("系统异常");
    public static final AliyunMediaException FILE_NOT_EXIST = new AliyunMediaException("文件不存在");
    public final static AliyunMediaException FILE_UPLOAD_ERROR = new AliyunMediaException("文件上传失败");
    public final static AliyunMediaException FILE_MERGE_ERROR = new AliyunMediaException("文件合并失败");
    public final static AliyunMediaException FILE_TRANSCODE_ERROR = new AliyunMediaException("文件转码失败");
    public final static AliyunMediaException PATH_NOT_EXIST = new AliyunMediaException("路径不存在");
    public final static AliyunMediaException FILE_DOWNLOAD_ERROR = new AliyunMediaException("文件下载到本地失败");
    public final static AliyunMediaException FILE_NOT_VIDEO = new AliyunMediaException("上传了非视频文件");
    public final static AliyunMediaException FILE_SAVE_ERROR = new AliyunMediaException("保存文件信息失败");
    public final static AliyunMediaException FILE_READ_ERROR = new AliyunMediaException("文件读取异常");
    public final static AliyunMediaException BATCH_UPDATE_REF_COUNT_ERROR = new AliyunMediaException("批量更新引用计数失败");
    public final static AliyunMediaException UPLOAD_FILE_PATH_NOT_NULL = new AliyunMediaException("上传文件路径不能为空");
    public final static AliyunMediaException UPLOAD_FILE_NOT_NULL = new AliyunMediaException("上传文件不能为空");
    public final static AliyunMediaException SAVE_FILE_PATH_NOT_NULL = new AliyunMediaException("保存文件路径不能为空");
    public final static AliyunMediaException FILE_NAME_ERROR = new AliyunMediaException("文件名称错误");
    public final static AliyunMediaException UPDATE_OBJECT_NAME_ERROR = new AliyunMediaException("视频转码-更新objectName失败");
    public final static AliyunMediaException GET_FILE_STREAM_ERROR = new AliyunMediaException("获取文件流失败");
    public final static AliyunMediaException UPLOAD_ERROR = new AliyunMediaException("分片上传失败");
    public final static AliyunMediaException DECREMENT_REF_COUNT_ERROR = new AliyunMediaException(" decrement 引用计数失败");
    public final static AliyunMediaException INCREMENT_REF_COUNT_ERROR = new AliyunMediaException(" increment 引用计数失败");
    public final static AliyunMediaException FILE_HASH_ERROR = new AliyunMediaException("文件散列校验失败");

}
