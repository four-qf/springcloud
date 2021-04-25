package com.qx.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

///**
// * @author: qiux
// * @date: 2021-04-21
// * @des: 缓存服务
// **/

@Component
public class RedisServcie {

    @Autowired
    private JedisCluster jedisCluster;

    public String get(String key) {

        return jedisCluster.get(key);

    }

}
