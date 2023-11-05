package com.example.springdemo.controller

import com.example.springdemo.annotation.UserLoginToken
import com.example.springdemo.bean.result.Result
import com.example.springdemo.bean.result.ArticleResult
import com.example.springdemo.dao.Article
import com.example.springdemo.service.ArticleService
import com.example.springdemo.utils.JWTUtil
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

@RestController
@RequestMapping("/article")
@CrossOrigin
class ArticleController {

    @Resource
    var articleService: ArticleService? = null

    @UserLoginToken
    @GetMapping("/add")
    fun addArticle(
        @RequestParam(value = "articleTitle") articleTitle: String,
        @RequestParam(value = "articleContent") articleContent: String,
        @RequestHeader(value = "token") token: String
    ): Result<ArticleResult> {
        val userId: Long = JWTUtil.getUserIdFromToken(token).toLong()
        val articleResult = articleService?.addArticle(articleTitle, articleContent, userId)
        return Result<ArticleResult>().success(articleResult)
    }

    @UserLoginToken
    @GetMapping("/get/all")
    fun queryAllArticles(
        @RequestHeader(value = "token") token: String
    ): Result<List<Article>> {
        val userId: Long = JWTUtil.getUserIdFromToken(token).toLong()
        val articles = articleService?.queryAllArticles(userId)
        return Result<List<Article>>().success(articles)
    }

    @UserLoginToken
    @GetMapping("/get")
    fun queryArticleById(
        @RequestParam(value = "articleId") articleId: Long,
    ): Result<Article> {
        val article = articleService?.queryArticleById(articleId)
        return Result<Article>().success(article)
    }

    @UserLoginToken
    @GetMapping("/update")
    fun updateArticle(
        @RequestParam(value = "articleTitle") articleTitle: String,
        @RequestParam(value = "articleContent") articleContent: String,
        @RequestParam(value = "articleId") articleId: Long,
    ): Result<ArticleResult> {
        val article = articleService?.updateArticle(articleTitle, articleContent, articleId)
        return Result<ArticleResult>().success(article)
    }

}