package com.example.springdemo.service

import com.example.springdemo.bean.vo.ArticleVO
import com.example.springdemo.bean.dao.Article

interface ArticleService {

    fun addArticle(articleTitle: String, articleContent: String, userId: Long): ArticleVO

    fun queryAllArticles(userId: Long): List<Article>

    fun queryArticleById(articleId: Long): Article?

    fun updateArticle(articleTitle: String, articleContent: String, articleId: Long): ArticleVO

}