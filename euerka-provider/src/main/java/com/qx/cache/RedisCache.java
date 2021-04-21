package com.qx.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: qiux
 * @date: 2021-04-21
 * @des: 缓存服务
 **/
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;



}
