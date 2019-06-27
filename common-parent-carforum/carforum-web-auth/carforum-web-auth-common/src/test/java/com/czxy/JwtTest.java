package com.czxy;

import com.czxy.carforum.auth.entity.UserInfo;
import com.czxy.carforum.auth.utils.JwtUtils;
import com.czxy.carforum.auth.utils.RasUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Administrator on 2019/3/1.
 */
public class JwtTest {

    private static final String pubKeyPath = "D:\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RasUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Test
    public void testGetRsa() throws Exception {
        this.publicKey = RasUtils.getPublicKey(pubKeyPath);
        this.privateKey = RasUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
//获得私钥
        PrivateKey privateKey = RasUtils.getPrivateKey(priKeyPath);
// 生成token
        String token = JwtUtils.generateToken(new UserInfo(20, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU1MTY2MTU2Mn0.JvauHdjcxogXE2cGGETmB0VaHaf-a2lJ406Yx80T5zdmrmLIYBSNpaHnB0Ph2NY6fqHFBWBruDhPEa3t_tz_5QYM6gRGpNNvGC2PamA3ofiLzuVcgDLSglZjX5NzULBQfMBpatH7_t7vn9LN6QQJi4IqIEdvGkVyE6AgPOLGkhs";
//获得公钥
        PublicKey publicKey = RasUtils.getPublicKey(pubKeyPath);
// 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());

    }

}


