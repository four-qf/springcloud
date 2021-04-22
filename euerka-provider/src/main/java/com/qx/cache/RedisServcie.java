package com.qx.cache;

import com.sun.org.omg.CORBA.Initializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: qiux
 * @date: 2021-04-21
 * @des: 缓存服务
 **/
@Configuration
@Component
public class RedisServcie implements InitializingBean {


    private JedisCluster jedisCluster;


    //    @Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes = Arrays.asList("47.115.3.241:6388", "47.115.3.241:6384", "47.115.3.241:6383", "47.115.3.241:6385", "47.115.3.241:6384", "47.115.3.241:6382", "81.71.31.103:6382", "81.71.31.103:6384", "47.115.3.241:6389");


    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     *
     * @return
     */
    @Bean("shareJedisCluster")
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : clusterNodes) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        //需要密码连接的创建对象方式
        return new JedisCluster(nodes, 10000, 1000, 1, new GenericObjectPoolConfig());
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {


        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .and()
                .build();
        // cluster模式
        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        redisConfig.setMaxRedirects(5);
        for (String ipPort : clusterNodes) {
            String[] ipPortArr = ipPort.split(":");
            redisConfig.clusterNode(ipPortArr[0], Integer.parseInt(ipPortArr[1].trim()));
        }

        return new JedisConnectionFactory(redisConfig, clientConfig);
    }


    public String get(String key) {

        return jedisCluster.get(key);

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jedisCluster = getJedisCluster();
    }
}
