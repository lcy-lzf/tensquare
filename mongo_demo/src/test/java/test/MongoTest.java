package test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MongoTest {

    private MongoClient mongoClient;
    private MongoDatabase commentdb;
    private MongoCollection<Document> comment;

    @Before
    public void init(){
        //1.创建操作MongoDB的客户端
        mongoClient = new MongoClient("182.92.204.73");
        //2.选择数据库
        commentdb = mongoClient.getDatabase("commentdb");
        //3.获取集合
        comment = commentdb.getCollection("comment");
    }

    //查询所有数据db.comment.find()
    @Test
    public void test1(){
        //4.使用集合进行查询
        FindIterable<Document> documents = comment.find();
        //5.解析结果集
        for (Document document : documents) {
            System.out.println("-------------------");
            System.out.println("_id:"+document.get("_id"));
            System.out.println("content:"+document.get("content"));
            System.out.println("userid:"+document.get("userid"));
            System.out.println("thumbup:"+document.get("thumbup"));
        }
    }

    @After
    public void after(){
        //释放资源
        mongoClient.close();
    }

    //根据_id查询
    @Test
    public void test2(){
        //封装查询条件
        BasicDBObject bson = new BasicDBObject("_id","6");
        FindIterable<Document> documents = comment.find(bson);
        for (Document document : documents) {
            System.out.println("-------------------");
            System.out.println("_id:"+document.get("_id"));
            System.out.println("content:"+document.get("content"));
            System.out.println("userid:"+document.get("userid"));
            System.out.println("thumbup:"+document.get("thumbup"));
        }
    }

    //新增
    @Test
    public void test3(){
        Map<String, Object> map = new HashMap<>();
        map.put("_id","6");
        map.put("content","新增测试");
        map.put("userid","1100");
        map.put("thumbup","2000");
        Document document = new Document(map);

        comment.insertOne(document);
    }
    //修改
    @Test
    public void test4(){
        Bson bson1 = new BasicDBObject("_id","6");
        Bson bson2 = new BasicDBObject("$set",new Document("content","新增测试"));
        comment.updateOne(bson1,bson2);
    }
    //删除
    @Test
    public void test5(){
        Bson bson = new BasicDBObject("_id","6");
        comment.deleteOne(bson);
    }
}
