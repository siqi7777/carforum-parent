package com.czxy.carforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Administrator on 2019/3/1.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WebAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAuthServiceApplication.class, args);
    }

}