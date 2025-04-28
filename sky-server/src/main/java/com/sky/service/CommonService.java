package com.sky.service;

import com.sky.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    
    Result<String> uploadFile(MultipartFile file);

    Result<String> deleteFile(String fileUrl);
}
