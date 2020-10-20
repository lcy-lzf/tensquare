package com.tensquare.article.repository;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {

    //springDataMongoDB支持通过方法名定义查询方式
    //根据文章id查询文章评论数据
    List<Comment> findByArticleid(String articleId);

//    //根据发布时间查询
//    List<Comment> findPublishdateAndThumbup(Date date,Integer thumbup);
//
//    //根据用户id查询，倒叙
//    List<Comment> findByUseridOrderByPublishdateDesc(String userid);
}