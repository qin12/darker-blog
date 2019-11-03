package com.darker.blog.orm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darker.blog.orm.entity.Tag;

import java.util.List;


public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findByArticleId(long id);
}
