package com.example.springdemo.bean.dao

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("article_info")
data class Article(
    @TableId(type = IdType.AUTO)
    val id: Long?,
    var articleId: Long?,
    var authorId: Long?,
    var articleTitle: String?,
    var articleContent: String?,
    var articlePrivacy: Int?,
    var gmtCreate: String?,
    var gmtUpdate: String?,
    var extendInfo: String?
) {
    constructor():
            this(null, null, null, null, null, null, null, null, null)
}
