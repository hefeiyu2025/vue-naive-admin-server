package com.naiveadmin.server.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 */
public interface IFileService {
    
    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 保存路径
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String path);
    
    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
} 