package com.darker.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.common.CommonConstant;
import com.darker.blog.common.QueryPage;
import com.darker.blog.exception.GlobalException;
import com.darker.blog.orm.entity.*;
import com.darker.blog.orm.mapper.ArticleMapper;
import com.darker.blog.vo.ArchivesWithArticle;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private TagService tagService;

    @Resource
    private ArticleCategoryService articleCategoryService;

    @Resource
    private ArticleTagService articleTagService;

    /**
     * 查询全部 已发布文章
     * @return
     */
    public List<Article> findAll() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getId);
        queryWrapper.eq(Article::getState, CommonConstant.DEFAULT_RELEASE_STATUS);
        IPage<Article> page = new Page<>(0, 8);
        return articleMapper.selectPage(page, queryWrapper).getRecords();
    }

    public IPage<Article> list(Article sysArticle, QueryPage queryPage) {
        IPage<Article> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getId);
        queryWrapper.like(StringUtils.isNotBlank(sysArticle.getTitle()), Article::getTitle, sysArticle.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(sysArticle.getAuthor()), Article::getAuthor, sysArticle.getAuthor());
        queryWrapper.eq(StringUtils.isNotBlank(sysArticle.getCategory()), Article::getCategory, sysArticle.getCategory());
        IPage<Article> selectPage = articleMapper.selectPage(page, queryWrapper);
        findInit(selectPage.getRecords());
        return selectPage;
    }

    public IPage<Article> findByPageForSite(QueryPage queryPage) {
        IPage<Article> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getId);
        queryWrapper.eq(Article::getState, CommonConstant.DEFAULT_RELEASE_STATUS);
        return articleMapper.selectPage(page, queryWrapper);
    }
    public Article findById(Long id) {
        Article sysArticle = articleMapper.selectById(id);
        List<Article> sysArticleList = new ArrayList<>();
        sysArticleList.add(sysArticle);
        findInit(sysArticleList);
        return sysArticle;
    }

    @Transactional
    public void add(Article sysArticle) {
        try {
            if (sysArticle.getState() == null) {
                sysArticle.setState(CommonConstant.DEFAULT_DRAFT_STATUS);
            }
            if (sysArticle.getPublishTime() == null && sysArticle.getState() == "1") {
                sysArticle.setPublishTime(new Date());
            }
            sysArticle.setAuthor(((User) SecurityUtils.getSubject().getPrincipal()).getUsername());
            sysArticle.setEditTime(new Date());
            sysArticle.setCreateTime(new Date());
            articleMapper.insert(sysArticle);
            sysArticle.setId(sysArticle.getId());
            //更新  文章  分类  标签
           this.updateArticleCategoryTags(sysArticle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void update(Article sysArticle) {
        if (sysArticle.getPublishTime() == null && sysArticle.getState().equals("1")) {
            sysArticle.setPublishTime(new Date());
        }
        articleMapper.updateById(sysArticle);
        //更新  文章  分类  标签
        updateArticleCategoryTags(sysArticle);
    }
    @Transactional
    public void delete(Long id) {
        if (id != null && id != 0) {
            articleMapper.deleteById(id);
            //删除文章-分类表的关联
            articleCategoryService.deleteByArticleId(id);
            //删除文章-标签表的关联
            articleTagService.deleteByArticleId(id);
        }
    }

    public List<ArchivesWithArticle> findArchives() {
        List<ArchivesWithArticle> archivesWithArticleList = new ArrayList<ArchivesWithArticle>();
        try {
            List<String> dates = articleMapper.findArchivesDates();
            dates.forEach(date -> {
                List<Article> sysArticleList = articleMapper.findArchivesByDate(date);
                ArchivesWithArticle archivesWithArticle = new ArchivesWithArticle(date, sysArticleList);
                archivesWithArticleList.add(archivesWithArticle);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(e.getMessage());
        }
        return archivesWithArticleList;
    }



    /**
     * 封装文章分类、标签数据
     *
     * @param list
     */
    private void findInit(List<Article> list) {
        if (!list.isEmpty()) {
            list.forEach(article -> {
                List<Category> sysCategoryList = categoryService.findByArticleId(article.getId());
                if (sysCategoryList.size() > 0) {
                    article.setCategory(sysCategoryList.get(0).getName());
                }
                List<Tag> tagList = tagService.findByArticleId(article.getId());
//                List<String> stringList = new ArrayList<>();
//                tagList.forEach(tags -> {
//                    stringList.add(tags.getName());
//                });
                article.setTags(tagList);
            });
        }
    }

    /**
     * 更新文章-分类-标签，三表间的关联
     *
     * @param sysArticle
     */
    private void updateArticleCategoryTags(Article sysArticle) {
        if (sysArticle.getId() != 0) {
            if (sysArticle.getCategory() != null) {
                articleCategoryService.deleteByArticleId(sysArticle.getId());
                Category sysCategory = categoryService.getById(sysArticle.getCategory());
                if (sysCategory != null) {
                    articleCategoryService.add(new ArticleCategory(sysArticle.getId(), sysCategory.getId()));
                }
            }
            if (sysArticle.getTags() != null && sysArticle.getTags().size() > 0) {
                articleTagService.deleteByArticleId(sysArticle.getId());
                sysArticle.getTags().forEach(tag -> {
                    articleTagService.add(new ArticleTag(sysArticle.getId(), tag.getId()));
                });
            }
        }
    }

}
