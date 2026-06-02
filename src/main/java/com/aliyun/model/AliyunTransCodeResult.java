package com.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 视频转码同步视图对象
 * 用于封装转码完成后的视频和封面信息，同步到业务系统
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AliyunTransCodeResult {

    /**
     * 转码后视频文件的存储路径
     * 包含文件名和扩展名的完整对象名称
     */
    private String transcodeVideoObjectName;

    /**
     * 转码后视频封面文件的存储路径
     * 包含文件名和扩展名的完整对象名称
     */
    private String transcodeVideoCoverObjectName;


}
