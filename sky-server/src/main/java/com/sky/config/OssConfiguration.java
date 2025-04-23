package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类, 用于创建aliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    // Bean注解用于项目启动时创建对象和注入到spring容器中管理
    @Bean
    @ConditionalOnMissingBean
    // 保证我们容器只有一个util对象, 没有则创建
    // 通过参数注入的方式进行注入进来
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象: {}", aliOssProperties);
        // 读取配置文件中的配置信息
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
