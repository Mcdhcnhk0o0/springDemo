package com.example.springdemo.bean.dao

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("user_info")
data class User(
    @TableId(type = IdType.AUTO)
    var userId: Long?,
    var email: String?,
    var userName: String?,
    var userNickname: String?,
    var password: String?,
    var extraInfo: String?
) {
    constructor() :
            this(null, null, null, null, null, null)
}