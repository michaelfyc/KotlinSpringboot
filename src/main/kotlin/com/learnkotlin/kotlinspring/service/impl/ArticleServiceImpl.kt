package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.mapper.ArticleMapper
import com.learnkotlin.kotlinspring.mapper.UserMapper
import com.learnkotlin.kotlinspring.service.IArticleService
import com.learnkotlin.kotlinspring.util.BadRequestException
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
        check(authorId > 0) {
            throw BadRequestException()
        }
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
        check(articleId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleServiceImpl.getArticleByArticleId>articleId: $articleId")
        return articleMapper.getArticleByArticleId(articleId)
    }
}
