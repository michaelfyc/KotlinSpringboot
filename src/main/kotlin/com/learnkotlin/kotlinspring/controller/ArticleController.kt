package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import com.learnkotlin.kotlinspring.service.impl.ArticleServiceImpl
import com.learnkotlin.kotlinspring.util.ResultVO
import com.learnkotlin.kotlinspring.util.StatusNotFound
import com.learnkotlin.kotlinspring.util.StatusOK
import com.learnkotlin.kotlinspring.util.annotations.FromToken
import com.learnkotlin.kotlinspring.util.annotations.NeedAuthorized
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    @PostMapping("/createPost")
    @NeedAuthorized
    fun createArticle(@Valid @RequestBody article: Article, @FromToken("uid") uid: Int): ResultVO {
        article.authorId = uid
        logger.info("<ArticleController.createArticle>article:$article")
        val articleId = articleServiceImpl.createArticle(article)
        return ResultVO(StatusOK("create post successfully"), articleId)
    }

    @GetMapping("/articles")
    fun listArticleByAuthorId(authorId: Int): ResultVO {
        require(authorId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleController.listArticleByAuthorId>authorId:$authorId")
        val articles = articleServiceImpl.listArticleByAuthorId(authorId)
        return ResultVO(StatusOK(), articles)
    }

    @GetMapping("/article")
    fun getArticleByArticleId(articleId: Int): ResultVO {
        require(articleId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleController.getArticleByArticleId>articleId:$articleId")
        val article =
            articleServiceImpl.getArticleByArticleId(articleId) ?: return ResultVO(StatusNotFound("article not found"))
        return ResultVO(StatusOK(), article)
    }

    @PostMapping("/hide")
    fun setVisibilityByArticleId(@RequestBody isVisible: Boolean, articleId: Int): ResultVO {
        require(articleId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleController.setVisibilityByArticleId>articleId:$articleId")
        articleServiceImpl.setVisibilityByArticleId(articleId, isVisible)
        return ResultVO(StatusOK("successfully set article $articleId visibility to $isVisible"))
    }
}
