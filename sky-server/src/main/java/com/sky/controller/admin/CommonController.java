package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    private static String FILE_UPLOAD_PATH = "D:\\upload\\";

    @Autowired
    // 阿里云工具类被创建了,所以可以直接使用依赖注入
    private AliOssUtil aliOssUtil;

    @Value("${sky.file.upload-dir}")
    private String uploadDir;

    @Value("${sky.file.access-url}")
    private String accessUrl; // 前端可访问的 URL 前缀

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}", file);

        /**
         * oss上传
         */
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取原始文件名后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 构建新文件名称
        String newFileName = UUID.randomUUID() + extension;

//
//        try {
//            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
//            return Result.success(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        /**
         * 本地上传
         */
        File dest = new File(uploadDir + File.separator + newFileName);
        try {
            file.transferTo(dest);
            // 返回前端可访问的 URL
            String fileUrl = accessUrl + "/" + newFileName;
            return Result.success(fileUrl);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }

    }

}
