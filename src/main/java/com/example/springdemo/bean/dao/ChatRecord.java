package com.example.springdemo.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("chat_record_info")
public class ChatRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fromWho;
    private Long toWho;
    private String message;
    private Boolean deleted;
    @TableField("gmt_create")
    private String gmtCreate;

}
