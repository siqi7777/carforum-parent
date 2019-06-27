package com.czxy.service.dao;

import com.czxy.carforum.pojo.Comment;
import com.czxy.carforum.pojo.Forum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2019/3/6.
 */
@org.apache.ibatis.annotations.Mapper
public interface CommentMapper extends Mapper<Comment> {


    @Select("select * from comment_tb where fid = #{fid}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "fid",column = "fid"),
            @Result(property = "cuid",column = "cuid"),
            @Result(property = "content",column = "content"),
            @Result(property = "createDate",column = "create_date"),
            @Result(property = "respContent",column = "resp_content"),

    })
    public List<Comment> findCommentByFid(@Param("fid") Integer fid);





}
