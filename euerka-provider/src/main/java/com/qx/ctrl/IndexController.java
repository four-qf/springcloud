package com.qx.ctrl;

import com.qx.cache.RedisServcie;
import com.qx.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private RedisServcie redisServcie;

    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/home")
    public String home() {
        return "hello world1";
    }

    @RequestMapping("/queryCache")
    public String queryCache(String key) {
        return redisServcie.get(key);
    }

    @RequestMapping("/test-sync")
    public String testSync() {
        asyncService.testSync();
        log.info("1232323----");
        return "ok";
    }

}
