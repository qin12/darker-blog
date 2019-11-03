package com.darker.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.component.PasswordHelper;
import com.darker.blog.orm.entity.User;
import com.darker.blog.orm.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends ServiceImpl<UserMapper, User>  {

    @Autowired
    private PasswordHelper passwordHelper;

    public User selectUserByUserName(String username) {
        return this.baseMapper.selectUserByUserName(username);
    }

    @Transactional
    public void insert(User user) {
        passwordHelper.encryptPassword(user); //加密
        this.baseMapper.insert(user);
    }


    @Transactional
    public void update(User user) {
        if (user.getPassword() != null && !"".equals(user.getPassword())) {
            passwordHelper.encryptPassword(user); //加密
        } else {
            user.setPassword(null);
            user.setSalt(null);
        }
        this.baseMapper.updateById(user);
    }
}
