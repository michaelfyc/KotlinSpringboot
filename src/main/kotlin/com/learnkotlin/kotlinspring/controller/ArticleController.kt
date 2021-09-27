package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.service.impl.ArticleServiceImpl
import com.learnkotlin.kotlinspring.util.BadRequestException
import com.learnkotlin.kotlinspring.util.ResultVO
import com.learnkotlin.kotlinspring.util.StatusNotFound
import com.learnkotlin.kotlinspring.util.StatusOK
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    @PostMapping("/createPost")
    fun createArticle(@Valid @RequestBody article: Article): ResultVO<Any> {
        logger.info("<ArticleController.createArticle>article:$article")
        val articleId = articleServiceImpl.createArticle(article)
        return ResultVO(StatusOK("create post successfully"), articleId)
    }

    @GetMapping("/articles")
    fun listArticleByAuthorId(authorId: Int): ResultVO<Any> {
        logger.info("<ArticleController.listArticleByAuthorId>authorId:$authorId")
        val articles = try {
            articleServiceImpl.listArticleByAuthorId(authorId)
        } catch (e: BadRequestException) {
            return ResultVO(e)
        }
        return ResultVO(StatusOK(), articles)
    }

    @GetMapping("/article")
    fun getArticleByArticleId(articleId: Int): ResultVO<Any> {
        logger.info("<ArticleController.getArticleByArticleId>articleId:$articleId")
        val article =
            articleServiceImpl.getArticleByArticleId(articleId) ?: return ResultVO(StatusNotFound("article not found"))
        return ResultVO(StatusOK(), article)
    }
}
