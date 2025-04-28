package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private CommonService commonService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> uploadFile(MultipartFile file) {
        return commonService.uploadFile(file);
    }


    /**
     * 删除文件
     * @param fileUrl
     * @return
     */
    @DeleteMapping("/deleteFile")
    @ApiOperation("文件删除")
    // 在删除方法前添加权限校验
    // @PreAuthorize("hasAuthority('admin')")

    // 添加操作日志记录（需要实现OperLogAspect）
    // @OperLog(operType = OperType.DELETE)
    public Result<String> deleteFile(@RequestParam String fileUrl) {
        return commonService.deleteFile(fileUrl);
    }
}
