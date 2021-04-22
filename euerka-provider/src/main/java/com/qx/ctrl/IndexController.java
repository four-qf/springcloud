package com.qx.ctrl;

import com.qx.cache.RedisServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private RedisServcie redisServcie;

    @RequestMapping("/home")
    public String home() {
        return "hello world1";
    }

    @RequestMapping("/queryCache")
    public String queryCache(String key) {
        return redisServcie.get(key);
    }

}
