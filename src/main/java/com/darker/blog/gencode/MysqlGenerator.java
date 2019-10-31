package com.darker.blog.gencode;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class MysqlGenerator extends AutoGenerator {
    @Override
    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        List<TableInfo> tableInfos =  super.getAllTableInfoList(config);
        tableInfos.forEach(t->{
            t.getFields().forEach(f->{
                if(StringUtils.isNotEmpty(f.getComment())) {
                    String comment = f.getComment();
                    comment = comment.replaceAll("\r\n" , "");
                    f.setComment(comment);
                }
            });
        });
        return tableInfos;
    }

}
