package com.darker.blog.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darker.blog.orm.entity.Category;

import java.util.List;


public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> findCategoryByArticleId(long id);
}
