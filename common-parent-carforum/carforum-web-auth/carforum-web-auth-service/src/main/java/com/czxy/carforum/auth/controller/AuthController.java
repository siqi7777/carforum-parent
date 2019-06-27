package com.czxy.carforum.auth.controller;

import com.czxy.carforum.auth.service.AuthService;
import com.czxy.carforum.pojo.User;
import com.czxy.carforum.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/3/4.
 */
@RestController
@RequestMapping
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("loginpwd")
    public ResponseEntity<BaseResult> login(@RequestBody User user){
        //登录 获得token
        String token = authService.login(user.getMobile(), user.getUpassword());
        //有token 就返回成功
        if(StringUtils.isNotBlank(token)){
            return ResponseEntity.ok(new BaseResult(1,"登录成功").append("token",token));
        };
        return ResponseEntity.ok(new BaseResult(0,"登录失败"));
    }

}
