package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置radis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化 (Redis 默认序列化工具)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());


        // TODO: 设置value的序列化 (Jackson 序列化工具)
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
