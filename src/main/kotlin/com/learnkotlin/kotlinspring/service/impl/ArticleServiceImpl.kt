package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import com.learnkotlin.kotlinspring.mapper.ArticleMapper
import com.learnkotlin.kotlinspring.mapper.UserMapper
import com.learnkotlin.kotlinspring.service.IArticleService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl : IArticleService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var articleMapper: ArticleMapper

    override fun listArticleByAuthorId(authorId: Int): ArrayList<Article>? {
        // first, see if author exists
        userMapper.selectById(authorId) ?: throw BadRequestException()
        // then, fetch all visible articles by author
        return articleMapper.listArticleByAuthorId(authorId)
    }

    override fun createArticle(article: Article): Int {
        logger.info("<ArticleServiceImpl.createArticle>Article: $article")
        articleMapper.insert(article)
        return article.articleId
    }

    override fun getArticleByArticleId(articleId: Int): Article? {
        logger.info("<ArticleServiceImpl.getArticleByArticleId>articleId: $articleId")
        return articleMapper.getArticleByArticleId(articleId)
    }

    override fun setVisibilityByArticleId(articleId: Int, isVisible: Boolean) {
        logger.info("<ArticleServiceImpl.setVisibilityByArticleId>articleId: $articleId")
        return articleMapper.setVisibilityByArticleId(articleId, isVisible)
    }
}
