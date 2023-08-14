package com.qx.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qiux
 * @Date 2023/8/14
 * @since
 */
@RestController
public class HealthController {


    @GetMapping("/hello")
    public String hello(){
        return "ok2";
    }

}
