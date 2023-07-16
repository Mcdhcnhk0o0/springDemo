package com.example.springdemo.service

import com.example.springdemo.bean.ArticleResult
import com.example.springdemo.dao.Article

interface ArticleService {

    fun addArticle(articleTitle: String, articleContent: String, userId: Long): ArticleResult

    fun queryAllArticles(userId: Long): List<Article>

    fun queryArticleById(articleId: Long): Article?

    fun updateArticle(articleTitle: String, articleContent: String, articleId: Long): ArticleResult

}