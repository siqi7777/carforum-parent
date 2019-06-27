package com.czxy.carforum.auth.service;


import com.czxy.carforum.auth.JwtProperties;
import com.czxy.carforum.auth.client.UserClient;
import com.czxy.carforum.auth.entity.UserInfo;
import com.czxy.carforum.auth.utils.JwtUtils;
import com.czxy.carforum.pojo.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/3/4.
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Resource
    private UserClient userClient;
    @Resource
    private JwtProperties jwtProperties;

    //登录
    public String login(String mobile,String upassword){
        try {
            //查询
            User user = userClient.queryUser(mobile, upassword).getBody();
            //若存在user 则生产token
            if(user!=null){
                return JwtUtils.generateToken(new UserInfo( user.getUid(),user.getUname()),jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            }
            //若为空
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
