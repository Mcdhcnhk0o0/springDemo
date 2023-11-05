package com.example.springdemo.bean.result

import com.example.springdemo.dao.Article
import com.example.springdemo.utils.ArticleResultInfo

data class ArticleResult (
    var success: Boolean? = null,
    var statusCode: Int? = null,
    var statusMessage: String? = null,
    var token: String? = null,
    var article: Article? = null
) {
    constructor():
            this(null, null, null, null, null)

    fun fail(info: ArticleResultInfo): ArticleResult {
        return ArticleResult(false, info.code, info.message, null, null)
    }

    fun success(article: Article, info: ArticleResultInfo): ArticleResult {
        return ArticleResult(true, info.code, info.message, null, article)
    }
}