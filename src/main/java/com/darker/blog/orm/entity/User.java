package com.darker.blog.orm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@TableName(value = "tb_user")
public class User implements Serializable {


    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @NotNull
    private String username;

    /**
     * 密码
     */
    @NotNull
    private String password;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 备注
     */
    private String remark;
}
