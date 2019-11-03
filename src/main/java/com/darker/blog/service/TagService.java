package com.darker.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.darker.blog.common.QueryPage;
import com.darker.blog.orm.entity.Tag;
import com.darker.blog.orm.mapper.TagMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagService articleTagService;


    public List<Tag> findAll() {
        return tagMapper.selectList(new QueryWrapper<>());
    }

    public IPage<Tag> list(Tag tag, QueryPage queryPage) {
        IPage<Tag> page = new Page<>(queryPage.getPage(), queryPage.getLimit());
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(tag.getName()), Tag::getName, tag.getName());
        queryWrapper.orderByDesc(Tag::getId);
        return tagMapper.selectPage(page, queryWrapper);
    }


    @Transactional
    public void add(Tag tag) {
        if (!exists(tag)) {
            tagMapper.insert(tag);
        }
    }

    private boolean exists(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tag.getName());
        return tagMapper.selectList(queryWrapper).size() > 0;
    }

    @Transactional
    public void update(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Transactional
    public void delete(Long id) {
        tagMapper.deleteById(id);

        //删除该标签与文章有关联的关联信息
        articleTagService.deleteByTagsId(id);
    }

    public List<Tag> findByArticleId(Long id) {
        return tagMapper.findByArticleId(id);
    }
}
