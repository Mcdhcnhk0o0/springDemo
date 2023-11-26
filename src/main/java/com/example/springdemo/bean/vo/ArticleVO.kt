package com.example.springdemo.bean.vo

import com.example.springdemo.bean.dao.Article
import com.example.springdemo.bean.vo.protocol.ArticleResultInfo

data class ArticleVO (
    var success: Boolean? = null,
    var statusCode: Int? = null,
    var statusMessage: String? = null,
    var token: String? = null,
    var article: Article? = null
) {
    constructor():
            this(null, null, null, null, null)

    fun fail(info: ArticleResultInfo): ArticleVO {
        return ArticleVO(false, info.code, info.message, null, null)
    }

    fun success(article: Article, info: ArticleResultInfo): ArticleVO {
        return ArticleVO(true, info.code, info.message, null, article)
    }
}