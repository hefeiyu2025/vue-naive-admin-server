package com.naiveadmin.server.service.impl;

import com.naiveadmin.server.common.exception.ServiceException;
import com.naiveadmin.server.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 本地文件上传服务实现类
 */
@Slf4j
@Service
public class LocalFileServiceImpl implements IFileService {

    @Value("${file.upload.path:/upload}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:/files}")
    private String urlPrefix;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            // 生成文件保存路径
            String fileName = generateFileName(file.getOriginalFilename());
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String filePath = uploadPath + "/" + path + "/" + datePath + "/" + fileName;

            // 创建目录
            Path directory = Paths.get(filePath).getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 保存文件
            file.transferTo(new File(filePath));

            // 返回文件访问URL
            return urlPrefix + "/" + path + "/" + datePath + "/" + fileName;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new ServiceException("文件上传失败");
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(urlPrefix)) {
            return false;
        }

        try {
            String filePath = uploadPath + fileUrl.substring(urlPrefix.length());
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("文件删除失败", e);
            return false;
        }
    }

    /**
     * 生成文件名
     * 使用UUID作为文件名，保留原文件扩展名
     */
    private String generateFileName(String originalFilename) {
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }
} 