package com.example.springdemo.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.example.springdemo.bean.vo.ArticleVO
import com.example.springdemo.bean.dao.Article
import com.example.springdemo.mapper.ArticleMapper
import com.example.springdemo.service.ArticleService
import com.example.springdemo.bean.vo.protocol.ArticleResultInfo
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource
import kotlin.random.Random


@Service
class ArticleServiceImpl: ArticleService {

    @Resource
    var articleMapper: ArticleMapper? = null

    override fun addArticle(articleTitle: String, articleContent: String, userId: Long): ArticleVO {
        val article: Article = Article()
        article.articleTitle = articleTitle
        article.articleContent = articleContent
        article.articleId = generateArticleId()
        article.gmtCreate = Date(System.currentTimeMillis()).toString()
        article.gmtUpdate = Date(System.currentTimeMillis()).toString()
        article.authorId = userId
        article.articlePrivacy = 0
        articleMapper?.insert(article)
        return ArticleVO().success(article, ArticleResultInfo.ARTICLE_CREATE_SUCCESS)
    }

    override fun queryAllArticles(userId: Long): List<Article> {
        val wrapper = QueryWrapper<Article>()
        wrapper.eq("author_id", userId)
        val articles = articleMapper?.selectList(wrapper)
        return articles ?: listOf()
    }

    override fun queryArticleById(articleId: Long): Article? {
        val wrapper = QueryWrapper<Article>()
        wrapper.eq("article_id", articleId)
        val articles = articleMapper?.selectList(wrapper)
        if (articles == null || articles.isEmpty()) {
            return null
        }
        return articles[0]
    }

    override fun updateArticle(articleTitle: String, articleContent: String, articleId: Long): ArticleVO {
        val article = queryArticleById(articleId) ?:
            return ArticleVO().fail(ArticleResultInfo.ARTICLE_NOT_EXIST)
        article.articleTitle = articleTitle
        article.articleContent = articleContent
        article.gmtUpdate = generateTimeStamp()
        val wrapper = UpdateWrapper<Article>()
        wrapper.eq("id", article.id)
        articleMapper?.update(article, wrapper)
        return ArticleVO().success(article, ArticleResultInfo.ARTICLE_UPDATE_SUCCESS)
    }

    private fun generateArticleId(): Long {
        return System.currentTimeMillis() + Random.nextInt()
    }

    private fun generateTimeStamp(): String {
        return Date(System.currentTimeMillis()).toString()
    }

}