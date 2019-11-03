package com.darker.blog.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.orm.entity.Article;
import com.darker.blog.orm.entity.ArticleTag;
import com.darker.blog.orm.entity.Tag;
import com.darker.blog.orm.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ArticleTagService extends ServiceImpl<ArticleTagMapper, ArticleTag>{

    @Transactional
    public void add(ArticleTag articleTag) {
        if (!exists(articleTag)) {
            this.baseMapper.insert(articleTag);
        }
    }

    private boolean exists(ArticleTag articleTag) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleTag.getArticleId());
        queryWrapper.eq(ArticleTag::getTagId, articleTag.getTagId());
        return this.baseMapper.selectList(queryWrapper).size() > 0;
    }


    public List<Tag> findByArticleId(Long articleId) {
        return  this.baseMapper.findByArticleId(articleId);
    }


    public List<ArticleTag> findByTagId(Long tagId) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, tagId);
        return  this.baseMapper.selectList(queryWrapper);
    }


    @Transactional
    public void deleteByArticleId(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        this.baseMapper.delete(queryWrapper);
    }


    @Transactional
    public void deleteByTagsId(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, id);
        this.baseMapper.delete(queryWrapper);
    }


    public List<Article> findByTagName(String tag) {
        return  this.baseMapper.findByTagName(tag);
    }
}
