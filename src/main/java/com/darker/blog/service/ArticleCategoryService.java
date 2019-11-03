package com.darker.blog.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.orm.entity.ArticleCategory;
import com.darker.blog.orm.mapper.ArticleCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ArticleCategoryService extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>{

    @Transactional
    public void add(ArticleCategory articleCategory) {
        if (!exists(articleCategory)) {
            this.baseMapper.insert(articleCategory);
        }
    }

    private boolean exists(ArticleCategory articleCategory) {
        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategory::getArticleId, articleCategory.getArticleId());
        queryWrapper.eq(ArticleCategory::getCategoryId, articleCategory.getCategoryId());
        return this.baseMapper.selectList(queryWrapper).size() > 0;
    }


    @Transactional
    public void deleteByArticleId(Long id) {
        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategory::getArticleId, id);
        this.baseMapper.delete(queryWrapper);
    }


    @Transactional
    public void deleteByCategoryId(Long id) {
        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategory::getCategoryId, id);
        this.baseMapper.delete(queryWrapper);
    }
}
