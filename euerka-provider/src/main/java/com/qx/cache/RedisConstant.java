package com.qx.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisCluster;


/**
 * @author: qiux
 * @date: 2021-04-25
 * @des:
 **/
@Configuration
public class RedisConstant {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public JedisCluster jedisCluster() {
        JedisClusterConnection connection = (JedisClusterConnection) redisConnectionFactory.getClusterConnection();
        return connection.getNativeConnection();
    }

}
