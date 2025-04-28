package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    private static String FILE_UPLOAD_PATH = "D:\\upload\\";

    @Autowired
    // 阿里云工具类被创建了,所以可以直接使用依赖注入
    private AliOssUtil aliOssUtil;


    @Value("${sky.file.upload-dir}")
    private String uploadDir;

    @Value("${sky.file.access-url}")
    private String accessUrl; // 前端可访问的 URL 前缀


    @Override
    public Result<String> uploadFile(MultipartFile file) {

        /**
         * oss上传
         */
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取原始文件名后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 构建新文件名称
        String newFileName = UUID.randomUUID() + extension;

        try {
            String filePath = aliOssUtil.uploadFile(file.getBytes(), newFileName);
            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         * 本地上传
         */
//        File dest = new File(uploadDir + File.separator + newFileName);
//        try {
//            file.transferTo(dest);
//            // 返回前端可访问的 URL
//            String fileUrl = accessUrl + "/" + newFileName;
//            return Result.success(fileUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    @Override
    public Result<String> deleteFile(String fileUrl) {
        try {
            // 从完整URL中提取OSS对象路径
            String objectName = extractObjectName(fileUrl);
            aliOssUtil.deleteFile(objectName);
            return Result.success(MessageConstant.FILE_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            return Result.error(MessageConstant.FILE_DELETE_FAILED);
        }
    }

    /**
     * 从完整URL中提取OSS对象名
     * 示例URL：https://bucket.oss-cn-shanghai.aliyuncs.com/images/abc.jpg
     * 提取结果：images/abc.jpg
     */
    private String extractObjectName(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String path = uri.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (URISyntaxException e) {
            throw new RuntimeException("非法文件路径", e);
        }
    }
}
