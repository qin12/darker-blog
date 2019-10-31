package com.darker.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.orm.entity.User;
import com.darker.blog.orm.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User>  {

    public User selectUserByUserName(String username) {
        return this.baseMapper.selectUserByUserName(username);
    }

    public void insert(User user) {
        this.baseMapper.insert(user);
    }
}
