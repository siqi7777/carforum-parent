package com.czxy.service.controller;

import com.alibaba.fastjson.JSON;
import com.czxy.carforum.pojo.Collect;
import com.czxy.carforum.pojo.Comment;
import com.czxy.carforum.pojo.Forum;
import com.czxy.carforum.pojo.User;
import com.czxy.carforum.vo.BaseResult;
import com.czxy.carforum.vo.CollectVo;
import com.czxy.carforum.vo.ComtVo;
import com.czxy.service.service.UserService;
import com.czxy.service.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Administrator on 2019/3/4.
 */
@RestController
@RequestMapping
public class ForumIndexController {

    @Resource
    private UserService userService;

    //获取用户信息 通用
    @GetMapping("/getUserInfo/{mobile}")
    public ResponseEntity<BaseResult> getUserInfo (@PathVariable("mobile") String mobile) {
        User user = userService.IfExist(mobile);
        return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",user));
    }

    //修改用户信息
    @PostMapping("/modifyfunc")
    public ResponseEntity<BaseResult> modifyfunc (@RequestBody User user) {
        try {
            userService.modifyfunc(user);
            return ResponseEntity.ok(new BaseResult(1,"修改成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"修改失败"));
        }
    }

    //上传头像
    /**
     * 图片文件上传
     */
    @PostMapping("/modifyImg")
    @ResponseBody
    public String addImage_User(@RequestParam(name = "image_data", required = false) MultipartFile file) {

        //文件上传
        if (!file.isEmpty()) {
            try {

                //图片命名
                String newCompanyImageName ="newPic"+new Random().nextInt(9999);
                String newCompanyImagepath = "D:\\ForumUserImg\\"+newCompanyImageName+".jpg";
                File newFile = new File(newCompanyImagepath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "图片上传失败！";
            } catch (IOException e) {
                e.printStackTrace();
                return "图片上传失败！";
            }
        }
        return "图片上传失败！";
    }


    //TODO 在myWrite.html页面中 查询 我的帖子（在发表帖子时，将数据存到redis中，在查询时从Redis中查）
    @GetMapping("/myForum/{mobile}")
    public ResponseEntity<BaseResult> myForum (@PathVariable("mobile") String mobile) {
        //从Redis中查询帖子
        List<Forum> forums = userService.myFrum(mobile);
        return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",forums));
    }

    //TODO 查询我的收藏（在收藏帖子时，就将数据存储到redis中，在取出时，也从redis中取。后续完成）
    //此处设计原因是 收藏的帖子被删除时，依然可以读取帖子主要信息只是详情不可见。
    @GetMapping("/selectCollect/{mobile}")
    public ResponseEntity<BaseResult> selectCollect (@PathVariable("mobile") String mobile) {
        List<Forum> collect = userService.selectCollect(mobile);
        return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",collect));
    }

    //删除当前帖子
    @GetMapping("/deleteForum/{fid}")
    public ResponseEntity<BaseResult> deleteForum (@PathVariable("fid") Integer fid) {
        try {
            userService.deleteForum(fid);
            return ResponseEntity.ok(new BaseResult(1,"删除成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"删除失败"));
        }
    }


    //取消收藏
    @PostMapping("/cancelCollect")
    public ResponseEntity<BaseResult> cancelCollect (@RequestBody Collect collect) {
        try {
            userService.cancelCollect(collect);
            return ResponseEntity.ok(new BaseResult(1,"取消收藏成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new BaseResult(0,"取消收藏失败").append("data",collect));
    }

    /**
     * 查询与我相关的消息
     */
    @GetMapping("/myMsg/{uid}")
    public ResponseEntity<BaseResult> myMsg (@PathVariable("uid") Integer uid) {
        try {
            List<Comment> comments = userService.myMsg(uid);
            return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",comments));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"查询失败"));
        }
    }

    /*
    *此处注释的原因：在上个方法中已将此功能实现 而且简单
    //查询评论者信息
    @PostMapping("/getCommentPerson")
    public ResponseEntity<BaseResult> getCommentPerson (@RequestBody Integer[] cuidArr) {
        try {
            List<User> users = userService.getCommentPerson(cuidArr);
            return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",users));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"查询失败"));
        }
    }
    */


    @GetMapping("/forumDetail/{fid}")
    public ResponseEntity<BaseResult> forumDetail (@PathVariable("fid") Integer fid) {
        try {
            Forum forums = userService.forumDetail(fid);
            return ResponseEntity.ok(new BaseResult(1,"查询成功").append("data",forums));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"查询失败"));
        }
    }


    //帖子收藏
    @PostMapping("/collectFunc")
    public ResponseEntity<BaseResult> collectFunc (@RequestBody CollectVo collectVo ) {
        try {
            Integer status = userService.collectFunc(collectVo);
            //判断收藏状态 给出不同响应
            if(status==1){
                return ResponseEntity.ok(new BaseResult(1,"收藏成功"));
            }else{
                return ResponseEntity.ok(new BaseResult(2,"取消收藏成功"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"收藏失败"));
        }

    }


    //帖子评论
    @PostMapping("/publishComt")
    public ResponseEntity<BaseResult> publishComt (@RequestBody ComtVo comtVo) {
        try {
            userService.publishComt(comtVo);
            //判断收藏状态 给出不同响应
            return ResponseEntity.ok(new BaseResult(1,"评论成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(0,"评论失败"));
        }

    }



}
