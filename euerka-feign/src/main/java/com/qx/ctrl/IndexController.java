package com.qx.ctrl;

import com.qx.OpenFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private OpenFeignClient openFeignClient;

    @GetMapping("/home")
    public Object home() {
        return "feign" + openFeignClient.home();
    }

}
