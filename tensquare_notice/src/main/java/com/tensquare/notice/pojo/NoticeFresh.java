package com.tensquare.notice.pojo;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tb_notice_fresh")
public class NoticeFresh {

    private String userId;
    private String noticeId;

    //set get...

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    @Override
    public String toString() {
        return "NoticeFresh{" +
                "userId='" + userId + '\'' +
                ", noticeId='" + noticeId + '\'' +
                '}';
    }
}
