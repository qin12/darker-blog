package com.darker.blog.orm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darker.blog.orm.entity.Article;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    List<String> findArchivesDates();

    List<Article> findArchivesByDate(String date);
}
