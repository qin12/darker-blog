package com.darker.blog.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darker.blog.orm.entity.User;

public interface UserMapper extends BaseMapper<User> {

    User selectUserByUserName(String username);

}

