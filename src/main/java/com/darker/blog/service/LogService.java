package com.darker.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.orm.entity.Log;
import com.darker.blog.orm.mapper.LogMapper;
import org.springframework.stereotype.Service;

@Service
public class LogService extends ServiceImpl<LogMapper, Log>  {
}
