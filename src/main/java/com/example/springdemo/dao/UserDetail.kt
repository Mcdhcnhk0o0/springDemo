package com.example.springdemo.dao

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.NoArgsConstructor


@NoArgsConstructor
@TableName("user_detail")
data class UserDetail (
    @TableId(type = IdType.AUTO)
    var id: Int?,
    var userId: Long?,
    var gender: String?,
    var birthday: String?,
    var avatar: String?,
    var address: String?,
    var introduction: String?,
    var extendInfo: String?
) {
    constructor():
        this(null, null, null, null, null, null, null, null)

}