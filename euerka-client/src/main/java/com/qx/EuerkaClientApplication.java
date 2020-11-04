package com.qx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableZuulProxy
@EnableEurekaClient
public class EuerkaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EuerkaClientApplication.class, args);
    }

}
