package com.czxy.carforum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/3/5.
 */
@Table(name="forum_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum {

    @Id
    @Column(name="forum_id")
    private Integer forumId;

    @Column(name="uid")
    private Integer uid;
    private User user;  //发帖人信息

    private List<Comment> comment;

    @Column(name="forum_theme")
    private String forumTheme;
    @Column(name="img")
    private String img;
    @Column(name="delivery_date")
    private String deliveryDate;
    @Column(name="thumb_num")
    private Integer thumbNum;
    @Column(name="browse_num")
    private Integer browseNum;
    @Column(name="url_info")
    private String urlInfo;
    @Column(name="content")
    private String content;
    @Column(name="collect_num")
    private Integer collectNum;








}
