package com.qx;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("eureka-provider")
public interface OpenFeignClient {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    String home();

}
