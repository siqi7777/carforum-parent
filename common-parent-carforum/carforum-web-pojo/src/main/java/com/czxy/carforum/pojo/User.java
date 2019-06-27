package com.czxy.carforum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2019/3/1.
 */
@Table(name="user_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Integer uid;
    @Column(name="mobile")
    private String mobile;
    @Column(name="upassword")
    private String upassword;
    @Column(name="nick_name")
    private String nickName;
    @Column(name="uname")
    private String uname;
    @Column(name="address")
    private String address;
    @Column(name="gender")
    private String gender;
    @Column(name="email")
    private String email;
    @Column(name="img")
    private String img;
    @Column(name="regist_date")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String registDate;
    @Column(name="theme_num")
    private Integer themeNum;
    @Column(name="greate_theme")
    private Integer greateTheme;
    @Column(name="regist_rank")
    private Integer registRank;







}
