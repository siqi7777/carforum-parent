package com.czxy.service.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.czxy.carforum.pojo.User;
import com.czxy.carforum.vo.BaseResult;
import com.czxy.carforum.vo.UserVo;
import com.czxy.service.dao.UserMapper;
import com.czxy.service.service.UserService;
import com.czxy.service.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/3/2.
 */
@RestController
@RequestMapping
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate redisTemplate;


    @PostMapping("/regist")
    public ResponseEntity<BaseResult> regist (@RequestBody UserVo uservo){
        //捕获异常 执行不同状态的结果
        try {
            //获取redis中存储的验证码
            String redisCode = redisTemplate.opsForValue().get(uservo.getMobile());
            System.out.println("页面传过来的checkcode："+uservo.getCheckcode()+"   user的mobile："+uservo.getMobile()+"     获取redis验证码："+redisCode);
            //与页面传入的验证码进行比对
            if(!uservo.getCheckcode().equals(redisCode)||uservo.getCheckcode()==null){
                redisTemplate.delete(uservo.getMobile());
                return ResponseEntity.ok(new BaseResult(0,"注册失败"));
            }
            userService.saveUser(uservo.getMobile());
            return ResponseEntity.ok(new BaseResult(1,"注册成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"注册失败"));
        }

    }

    @PostMapping("/sms")
    public ResponseEntity<BaseResult> sendSms(@RequestBody User user){
        try {
            //1.生成验证码
            String randomCode = RandomStringUtils.randomNumeric(4);
            System.out.println("本次生成的验证码为 "+randomCode);
            //存放到Redis
            redisTemplate.opsForValue().set(user.getMobile(),randomCode, 1,TimeUnit.HOURS);
//            redisTemplate.opsForValue().set("keyy","123", 1,TimeUnit.HOURS);
            System.out.println("redis存放的验证码为 "+redisTemplate.opsForValue().get(user.getMobile()));

            //2.发短信
            SendSmsResponse smsRes = SmsUtil.sendSms(user.getMobile(), "9329", randomCode, "小润发超市", "");
            //判断验证码是否发送成功
            if("ok".equalsIgnoreCase(smsRes.getCode())){
                return ResponseEntity.ok(new BaseResult(1,"验证码发送成功"));
            }else{
                return ResponseEntity.ok(new BaseResult(0,smsRes.getMessage()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"验证码发送异常"));
        }

    }

    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("mobile") String mobile , @RequestParam("upassword") String password){
        //通过手机号查询用户
        User user = userService.IfExist(mobile);
        //判断密码是否正确
        if(user==null || !user.getUpassword().equals(password)){
            return ResponseEntity.ok(null);
        }
        //否则 正确
        return ResponseEntity.ok(user);


    };







}
