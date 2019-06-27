package com.czxy.carforum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2019/3/5.
 */
@Table(name="collect_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collect {

    @Id
    @Column(name="id")
    private Integer id;
    @Column(name="fid")
    private Integer fid;
    @Column(name="uid")
    private Integer uid;

}
