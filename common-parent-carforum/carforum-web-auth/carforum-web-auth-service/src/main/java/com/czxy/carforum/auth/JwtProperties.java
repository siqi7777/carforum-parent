package com.czxy.carforum.auth;

import com.czxy.carforum.auth.utils.RasUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Administrator on 2019/3/1.
 */
    @Data
    @ConfigurationProperties(prefix = "sc.jwt")
    public class JwtProperties {
        private String secret;          // 密钥

        private String pubKeyPath;      // 公钥路径

        private String priKeyPath;      // 私钥路径

        private int expire;             // token过期时间

        private PublicKey publicKey;    // 公钥对象

        private PrivateKey privateKey; // 私钥对象

        //进行初始化操作
        @PostConstruct
        public void init(){

            try {
                // 路径 --> 对象  （如果不存在，创建一次）
                //1 获得私钥或公钥的文件
                File pubFile = new File( this.pubKeyPath );
                File priFile = new File( this.priKeyPath );

                //2 文件不存在，生产一对。
                if( ! pubFile.exists() || !priFile.exists()) {
                    RasUtils.generateKey( this.pubKeyPath ,this.priKeyPath , this.secret  );
                }

                //3 获得对应对象
                this.publicKey = RasUtils.getPublicKey( this.pubKeyPath );
                this.privateKey = RasUtils.getPrivateKey( this.priKeyPath );

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
