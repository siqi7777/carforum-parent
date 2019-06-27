package com.czxy.service.service;

import com.czxy.carforum.pojo.Collect;
import com.czxy.carforum.pojo.Comment;
import com.czxy.carforum.pojo.Forum;
import com.czxy.carforum.pojo.User;
import com.czxy.carforum.vo.CollectVo;
import com.czxy.carforum.vo.ComtVo;
import com.czxy.service.dao.CollectMapper;
import com.czxy.service.dao.CommentMapper;
import com.czxy.service.dao.ForumMapper;
import com.czxy.service.dao.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2019/3/2.
 */
@Service
@Transactional
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ForumMapper forumMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CommentMapper commentMapper;


    public void saveUser(String mobile) {
        int randomPwd = new Random().nextInt(1000000);
        User user = new User();
        Date registTime = new Date();
        user.setRegistDate(registTime.toLocaleString());
        user.setMobile(mobile);
//        String unameran = "qwertyuodjfkjshdf123456789lkjsdzv";
//        unameran.subSequence(0,1);
//        int randomUname = new Random().nextInt(1000000);
//        String randomUname = new Random().
//        user.setUname();
        user.setUpassword(randomPwd+"");
        //此处应该优化 判断用是否存在
        userMapper.insert(user);
    }

    //验证用户是否注册过
    public User IfExist(String mobile){
        //比较mobile
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",mobile);
        //查询是否有当前mobile
        User user2 = userMapper.selectOneByExample(example);
        //对查询结果做出响应
        return user2;
    }

    //通过手机号修改用户信息
    public void modifyfunc(User user) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",user.getMobile());
        User user1 = userMapper.selectOneByExample(example);
        user1.setUpassword(user.getUpassword());
        userMapper.updateByPrimaryKey(user1);
    }

    //查询我的帖子
    public List<Forum> myFrum(String mobile) {
        //1.先查询用户
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",mobile);
        User user = userMapper.selectOneByExample(example);
        //2.查询帖子
        Example example2 = new Example(Forum.class);
        Example.Criteria criteria2 = example2.createCriteria();
        //查询这个用户发过的所有帖子
        criteria2.andEqualTo("uid",user.getUid());
        List<Forum> forums = forumMapper.selectByExample(example2);
        return forums;
    }

    //查看我的收藏
    public List<Forum> selectCollect (String mobile) {
        //0. 先查询当前用户的uid
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",mobile);
        User user= userMapper.selectOneByExample(example);
        //1.再通过用户uid查看他收藏的所有帖子
        Example example2 = new Example(Collect.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("uid",user.getUid());
        List<Collect> collects = collectMapper.selectByExample(example2);
        //2.将查询到的fid存入新的集合中
        List<Integer> fids = new ArrayList<>();
        List<Forum> forumList = new ArrayList<>();

        for (Collect thisC : collects) {
//            fids.add(thisC.getFid());
            Forum forums = forumMapper.findFrums(thisC.getFid());
            forumList.add(forums);
        }
        //3.通过fid查询具体帖子信息

        return forumList;
    }


    /**
     * 删除帖子
     * @param fid
     */
    public void deleteForum(Integer fid) {
        forumMapper.deleteByPrimaryKey(fid);
    }


    /**
     * 取消收藏
     * @param collect
     */
    public void cancelCollect(Collect collect) {
        //将收藏数量减一
        Forum thisForum = forumMapper.selectByPrimaryKey(collect.getFid());
        thisForum.setCollectNum(thisForum.getCollectNum()-1);
        System.out.println(thisForum.getCollectNum());
        forumMapper.updateByPrimaryKey(thisForum);

        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fid",collect.getFid());
        criteria.andEqualTo("uid",collect.getUid());
        //查询出对应的收藏行
        Collect collect1 = collectMapper.selectOneByExample(example);
        //再通过主键删除收藏信息
        collectMapper.deleteByPrimaryKey(collect1);
//        //将收藏数量减一     这段代码放在最后执行 会报错
//        Forum thisForum = forumMapper.findFrums(collect.getFid());
//        System.out.println(thisForum.getCollectNum());
//        thisForum.setCollectNum(thisForum.getCollectNum()-1);
    }


    /**
     * 查看我的消息
     * @param uid
     */
    public List<Comment> myMsg(Integer uid) {
        Example example = new Example(Forum.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid",uid);
        //查询到了楼主发表的所有帖子
        List<Forum> forums = forumMapper.selectByExample(example);
        //将所有相关评论信息存储到集合中
        ArrayList<Comment> commentsList = new ArrayList<Comment>();
        //通过帖子id查询用户相关评论
        for (Forum thisForum : forums) {
            List<Comment> thisCommentList= commentMapper.findCommentByFid(thisForum.getForumId());
            for(Comment thisComment:thisCommentList){

                //查询用户
                User thisUser = userMapper.selectByPrimaryKey(thisComment.getCuid());
                //将用户存到评论中
                thisComment.setUser(thisUser);
                //将查询到的相关评论add进去
                commentsList.add(thisComment);

            }
        }
        return commentsList;
    }



    /*
    //查询评论者信息
    public List<User> getCommentPerson(Integer[] cuids) {
        List<User> users = new ArrayList<User>();
        for(Integer thisId:cuids){
            User user = userMapper.selectByPrimaryKey(thisId);
            users.add(user);
        }
        return users;
    }
    */


    //查询帖子详情
    public Forum forumDetail(Integer fid) {
        //查询此帖子详情
        Forum forum = forumMapper.selectByPrimaryKey(fid);
        //查询楼主详情
        User user = userMapper.selectByPrimaryKey(forum.getUid());
        forum.setUser(user);
        //查询评论过此帖子的人（个人详情、评论内容详情）
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fid",fid);
        //创建新集合 以便存储下面的多条评论
        ArrayList<Comment> commentsList = new ArrayList<Comment>();
        //查询到了所有评论
        List<Comment> comments = commentMapper.selectByExample(example);
        //根据评论查询评论者信息
        for(Comment thisComment:comments){
            //查询用户
            User thisUser = userMapper.selectByPrimaryKey(thisComment.getCuid());
            //将用户存到评论中
            thisComment.setUser(thisUser);
            //将查询到的相关评论add进去
            commentsList.add(thisComment);
        }
        //将多条评论存到当前帖子中
        forum.setComment(commentsList);
        return forum;
    }

    //添加收藏
    public Integer collectFunc(CollectVo collectVo) {
        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fid",collectVo.getFid());
        criteria.andEqualTo("uid",collectVo.getUid());
        Collect collect1 = collectMapper.selectOneByExample(example);
        //判断用户是否收藏过 没收藏过 就收藏 收藏过 就取消收藏 返回不同值
        if(collect1!=null){

            collectMapper.deleteByPrimaryKey(collect1);
            return 2;   //取消收藏成功
        }else{
            Collect collect = new Collect();
            collect.setUid(collectVo.getUid());
            collect.setFid(collectVo.getFid());
            collectMapper.insert(collect);
            return 1;   //收藏成功
        }

    }


    //评论帖子
    public void publishComt(ComtVo comtVo) {
        Comment comment = new Comment();
        comment.setContent(comtVo.getContent());
        User user = userMapper.selectByPrimaryKey(comtVo.getUid());
        comment.setUser(user);
        comment.setFid(comtVo.getForumId());
        comment.setCreateDate(new Date().toLocaleString());
        commentMapper.insert(comment);
    }


}
