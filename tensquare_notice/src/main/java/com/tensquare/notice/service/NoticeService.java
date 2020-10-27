package com.tensquare.notice.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.notice.client.ArticleClient;
import com.tensquare.notice.client.UserClient;
import com.tensquare.notice.dao.NoticeDao;
import com.tensquare.notice.dao.NoticeFreshDao;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeFreshDao noticeFreshDao;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private UserClient userClient;

    //完善消息内容
    private void getInfo(Notice notice){
        Result userResult = userClient.selectById(notice.getOperatorId());
        HashMap userMap = (HashMap) userResult.getData();
        String nickname = userMap.get("nickname").toString();
        notice.setOperatorName(nickname);
        Result articleResult = articleClient.findById(notice.getTargetId());
        HashMap articleMap = (HashMap) articleResult.getData();
        String title = articleMap.get("title").toString();
        notice.setTargetName(title);

    }

    public Notice selectById(String id) {
        Notice notice = noticeDao.selectById(id);
        getInfo(notice);
        return notice;
    }

    public Page<Notice> selectByPage(Notice notice, Integer page, Integer size) {
        Page<Notice> noticePage = new Page<>(page,size);
        List<Notice> noticeList = noticeDao.selectPage(noticePage, new EntityWrapper<>(notice));
        for (Notice n : noticeList) {
            getInfo(n);
        }

        noticePage.setRecords(noticeList);
        return noticePage;
    }

    public void save(Notice notice) {
        notice.setState("0");//0未读，1已读
        notice.setCreatetime(new Date());

        //分布式id生成器
        long id = idWorker.nextId();
        notice.setId(String.valueOf(id));
        //待推送消息入库，新消息提醒
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setNoticeId(String.valueOf(id));
        noticeFresh.setUserId(notice.getReceiverId());
        noticeFreshDao.insert(noticeFresh);
        noticeDao.insert(notice);
    }

    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    public Page<NoticeFresh> freshPage(String userId, Integer page, Integer size) {
        Page<NoticeFresh> pageData= new Page<>(page,size);
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setUserId(userId);
        List<NoticeFresh> noticeFreshes = noticeFreshDao.selectPage(pageData, new EntityWrapper<>(noticeFresh));
        pageData.setRecords(noticeFreshes);
        return pageData;
    }

    public void freshDelete(NoticeFresh noticeFresh) {
        noticeFreshDao.delete(new EntityWrapper<>(noticeFresh));
    }
}
