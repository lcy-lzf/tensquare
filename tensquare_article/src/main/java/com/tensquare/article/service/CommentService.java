package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(String commentId) {
        Optional<Comment> optional = commentRepository.findById(commentId);

        if (optional.isPresent())return optional.get();
        return null;
    }

    public void save(Comment comment) {
        //分布式id生成器生成id
        String id = idWorker.nextId()+"";
        //初始化数据
        comment.set_id(id);
        comment.setThumbup(0);
        comment.setPublishdate(new Date());
        //保存数据
        commentRepository.save(comment);
    }

    public void updateById(Comment comment) {
        //save方法，如果主键存在，则修改
        commentRepository.save(comment);
    }

    public void deleteById(String commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByArticleId(String articleId) {
        List<Comment> l = commentRepository.findByArticleid(articleId);
        return l;
    }

    public void thumbup(String commentId) {
//        //根据评论id查询数据
//        Optional<Comment> comment = commentRepository.findById(commentId);
//        Comment comment1 = comment.get();
//        //对评论点赞数据+1
//        comment1.setThumbup(comment1.getThumbup()+1);
//        //保存修改数据
//        commentRepository.save(comment1);
        //直接修改数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(commentId));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"comment");
    }
}
