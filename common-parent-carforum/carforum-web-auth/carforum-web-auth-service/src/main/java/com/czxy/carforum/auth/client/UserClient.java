package com.czxy.carforum.auth.client;

import com.czxy.carforum.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2019/3/4.
 */
@FeignClient(value="web-service")
public interface UserClient {

    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("mobile") String mobile, @RequestParam("upassword") String password);

}