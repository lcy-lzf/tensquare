package com.tensquare.encrypt.user.service;

import com.tensquare.encrypt.user.dao.UserDao;
import com.tensquare.encrypt.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    //根据id查询用户
    public User selectById(String userId){
        return userDao.selectById(userId);
    }
}
