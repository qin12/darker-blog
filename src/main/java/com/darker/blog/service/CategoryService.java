package com.darker.blog.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.common.QueryPage;
import com.darker.blog.orm.entity.Category;
import com.darker.blog.orm.mapper.CategoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleCategoryService articleCategoryService;


    public IPage<Category> list(Category sysCategory, QueryPage queryPage) {
        IPage<Category> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(sysCategory.getName()), Category::getName, sysCategory.getName());
        queryWrapper.orderByDesc(Category::getId);
        return categoryMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    public void add(Category sysCategory) {
        if (!exists(sysCategory)) {
            categoryMapper.insert(sysCategory);
        }
    }

    @Transactional
    public void update(Category sysCategory) {
        categoryMapper.updateById(sysCategory);
    }

    private boolean exists(Category sysCategory) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, sysCategory.getName());
        return categoryMapper.selectList(queryWrapper).size() > 0 ? true : false;
    }

    @Transactional
    public void delete(Long id) {
        categoryMapper.deleteById(id);
        //删除与该分类与文章关联的信息
        articleCategoryService.deleteByCategoryId(id);
    }


    public List<Category> findByArticleId(Long id) {
        return categoryMapper.findCategoryByArticleId(id);
    }


    public Category findByName(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, name);
        List<Category> list = categoryMapper.selectList(queryWrapper);
        return list.size() > 0 ? list.get(0) : null;
    }
}
