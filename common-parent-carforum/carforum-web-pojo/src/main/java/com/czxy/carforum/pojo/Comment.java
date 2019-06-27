package com.czxy.carforum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2019/3/6.
 */
@Table(name="comment_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @Column(name="id")
    private Integer id;
    @Column(name="fid")
    private Integer fid;

    @Column(name="cuid")
    private Integer cuid;
    private User user;

    @Column(name="content")
    private String content;
    @Column(name="create_date")
    private String createDate;
    @Column(name="resp_content")
    private String respContent;
    @Column(name="resp_status")
    private Integer respStatus;



}
