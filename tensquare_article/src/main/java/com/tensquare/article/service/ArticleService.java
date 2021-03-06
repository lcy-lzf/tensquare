package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<Article> findAll(){
        return articleDao.selectList(null);
    }

    public Article findById(String articleId) {
        return articleDao.selectById(articleId);
    }

    public void save(Article article) {
        //使用分布式ID生成器
        String id = String.valueOf(idWorker.nextId());
        article.setId(id);

        //初始化数据
        article.setVisits(0);//浏览量
        article.setThumbup(0);//点赞数
        article.setComment(0);//评论数

        //新增
        articleDao.insert(article);
    }

    public void updateById(Article article) {
        // 根据主键id修改
        articleDao.updateById(article);
//        // 根据条件修改
//        // 创建条件对象
//        EntityWrapper<Article> wrapper = new EntityWrapper<>();
//        wrapper.eq("id",article.getId());
//        // 设置条件
//        articleDao.update(article,wrapper);
    }

    public void deleteById(String articleId) {
        articleDao.deleteById(articleId);
    }

    public Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size) {
        //设置查询条件
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet){
            wrapper.eq(map.get(key)!=null,key,map.get(key));
        }
        //设置参数分页
        Page<Article> pageData = new Page<>(page,size);

        //执行查询
        List<Article> list = articleDao.selectPage(pageData, wrapper);
        pageData.setRecords(list);

        //返回
        return pageData;
    }

    public Boolean subscribe(String articleId, String userId) {
        //根据文章id查询文章作者id
        String authorId = articleDao.selectById(articleId).getUserid();
        //查询用户的订阅关系，是否订阅了该作者
        String userKey = "article_subscribe_"+userId;
        String authorKey = "article_author_" + authorId;
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);
        //如果订阅作者，就取消订阅
        if (flag){
            redisTemplate.boundSetOps(userKey).remove(authorId);
            redisTemplate.boundSetOps(authorKey).remove(userId);
            return false;
        }else {
            //如果没订阅，就订阅
            redisTemplate.boundSetOps(userKey).add(authorId);
            redisTemplate.boundSetOps(authorKey).add(userId);
            return true;
        }


    }
}
