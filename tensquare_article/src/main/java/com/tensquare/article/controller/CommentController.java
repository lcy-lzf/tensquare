package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;

    //GET /comment 查询所有评论
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Comment> list = commentService.findAll();

        return new Result(true, StatusCode.OK,"查询成功",list);
    }
    //GET /comment/{commentId} 根据评论id查询评论
    @RequestMapping(value = "{commentId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String commentId){
        Comment comment = commentService.findById(commentId);
        return new Result(true, StatusCode.OK,"查询成功",comment);
    }

    //POST /comment 新增评论
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentService.save(comment);
        return new Result(true,StatusCode.OK,"新增成功");
    }

    //PUT /comment/{commentId} 修改评论
    @RequestMapping(value = "{commentId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String commentId,
                             @RequestBody Comment comment){
        //设置评论主键
        comment.set_id(commentId);
        //执行修改
        commentService.updateById(comment);

        return new Result(true,StatusCode.OK,"修改成功");
    }
    //DELETE /comment/{commentId} 根据id删除评论
    @RequestMapping(value = "{commentId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String commentId){

        //执行删除
        commentService.deleteById(commentId);

        return new Result(true,StatusCode.OK,"删除成功");
    }

    //GET /comment/article/{articleId} 根据文章id查询评论
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId){
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    //PUT /comment/thumbup/{commentId} 根据评论id点赞
    @RequestMapping(value = "/thumbup/{commentId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String commentId){
        //把用户点赞信息存放到Redis中，禁止重复点赞
        //模拟用户id
        String userId = "123";

        //判断查询到的结果是否为空
        Object flag = redisTemplate.opsForValue().get("thumbup_" + userId + "_" + commentId);
        if (flag==null){
            redisTemplate.opsForValue().set("thumbup_" + userId + "_" + commentId,1);
            commentService.thumbup(commentId);
            return new Result(true,StatusCode.OK,"点赞成功");
        }
        return new Result(false,StatusCode.REPERROR,"重复点赞");
    }
}
