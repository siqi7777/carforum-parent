package com.czxy.service.dao;
import com.czxy.carforum.pojo.Forum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2019/3/5.
 */
@org.apache.ibatis.annotations.Mapper
public interface ForumMapper extends Mapper<Forum> {

    @Select("select * from forum_tb where forum_id=#{fids}")
    @Results({
          @Result(property = "forumId" ,column = "forum_id" ),
            @Result(property = "deliveryDate" ,column = "delivery_date" ),
            @Result(property = "browseNum" ,column = "browse_num" ),
            @Result(property = "forumTheme" ,column = "forum_theme" )

    })
    public Forum findFrums(@Param("fids") Integer fids);
}
