package com.tensquare.article.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // GET /article 查询全部文章
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Article> list = articleService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    //  GET /article/{articleId} 根据ID查询文章
    @RequestMapping(value = "/{articleId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String articleId){
        Article article = articleService.findById(articleId);
        return new Result(true,StatusCode.OK,"查询成功",article);
    }

    // POST /article 增加文章
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true,StatusCode.OK,"新增成功");
    }

    // PUT /article/{articleId} 修改文章
    @RequestMapping(value = "/{articleId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String articleId,
                             @RequestBody Article article){
        //设置id
        article.setId(articleId);
        //执行修改
        articleService.updateById(article);

        return new Result(true,StatusCode.OK,"修改成功");
    }

    // DELETE /article/{articleId} 根据ID删除文章
    @RequestMapping(value = "/{articleId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String articleId){
        articleService.deleteById(articleId);

        return new Result(true,StatusCode.OK,"删除成功");
    }

    // POST /article/search/{page}/{size} 文章分页
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    //根据条件查询，所有条件都需要判断，map便于遍历
    //遍历pojo的所有属性需要使用反射，成本高，性能低
    public Result findByPage(@PathVariable Integer page,
                             @PathVariable Integer size,
                             @RequestBody Map<String,Object> map){
        //根据条件分页查询
        Page<Article> pageData = articleService.findByPage(map,page,size);
        //封装分页范围对象
        PageResult<Article> pageResult = new PageResult<>(
                pageData.getTotal(),pageData.getRecords()
        );
        //返回数据
        return new Result(true,StatusCode.OK,"查询成功",pageResult);

    }

    // Jrebel 测试
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String testJrebel(){
        return "Jrebel更新成功";
    }
    //测试公共异常类
    @RequestMapping(value = "exception",method = RequestMethod.GET)
    public Result testException(){
        int a = 1/0;

        return null;
    }

}
