package com.example.minispringmvc.framework.sample.service;

import com.example.minispringmvc.framework.annotation.Service;
import com.example.minispringmvc.framework.sample.pojo.User;

/**
 * @author kangdongyang
 * @version 1.0
 * @description:
 * @date 2024/8/13 11:46
 */
@Service
public class UserServiceImpl implements UserService{
    @Override
    public User getUser() {
        return new User("kkk","yyy");
    }
}
