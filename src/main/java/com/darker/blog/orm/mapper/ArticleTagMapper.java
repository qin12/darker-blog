package com.darker.blog.orm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darker.blog.orm.entity.Article;
import com.darker.blog.orm.entity.ArticleTag;
import com.darker.blog.orm.entity.Tag;

import java.util.List;


public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    List<Tag> findByArticleId(long articleId);

    List<Article> findByTagName(String name);
}
