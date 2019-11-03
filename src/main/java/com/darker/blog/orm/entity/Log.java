package com.darker.blog.orm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_log")
public class Log implements Serializable {

    private static  final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    private String username;

    private String method;

    private String params;

    private String operation;

    private String errorMessage;

    private String ip;

     private Date createTime;

    private String location;

}
