package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import com.learnkotlin.kotlinspring.service.impl.ArticleServiceImpl
import com.learnkotlin.kotlinspring.util.ResultVO
import com.learnkotlin.kotlinspring.util.StatusNotFound
import com.learnkotlin.kotlinspring.util.StatusOK
import com.learnkotlin.kotlinspring.util.annotations.FromToken
import com.learnkotlin.kotlinspring.util.annotations.NeedRole
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    @PostMapping("/createPost")
    @NeedRole
    fun createArticle(@Valid @RequestBody article: Article, @FromToken("uid") uid: Int): ResultVO {
        article.authorId = uid
        logger.info("<ArticleController.createArticle>article:$article")
        val articleId = articleServiceImpl.createArticle(article)
        return ResultVO(StatusOK("create post successfully"), mapOf("article_id" to articleId))
    }

    @GetMapping("/user/articles")
    fun listArticleByAuthorId(@RequestParam("uid") authorId: Int): ResultVO {
        require(authorId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleController.listArticleByAuthorId>authorId:$authorId")
        val articles = articleServiceImpl.listArticleByAuthorId(authorId)
        return ResultVO(StatusOK(), articles)
    }

    @GetMapping("/articles")
    fun listArticles(@FromToken("role") role: CommonRoles?): ResultVO {
        val userRole = role ?: CommonRoles.GUEST
        val articles = articleServiceImpl.listArticles(userRole)
        return ResultVO(StatusOK(), articles)
    }

    @GetMapping("/article")
    fun getArticleByArticleId(articleId: Int, @FromToken("role") role: CommonRoles?): ResultVO {
        require(articleId > 0) {
            throw BadRequestException()
        }
        // if visitor hasn't signed in, there will be no token.
        // therefore, assign a guest role for them
        val userRole = role ?: CommonRoles.GUEST
        logger.info("<ArticleController.getArticleByArticleId>articleId:$articleId")
        val article =
            articleServiceImpl.getArticleByArticleId(articleId, userRole)
                ?: return ResultVO(StatusNotFound("article not found"))
        return ResultVO(StatusOK(), article)
    }

    @PostMapping("/hide")
    @NeedRole(CommonRoles.ADMIN)
    fun setArticleAccessibleRole(@RequestBody role: CommonRoles, articleId: Int): ResultVO {
        require(articleId > 0) {
            throw BadRequestException()
        }
        logger.info("<ArticleController.setVisibilityByArticleId>articleId:$articleId")
        articleServiceImpl.setArticleAccessibleRole(articleId, role)
        return ResultVO(StatusOK("successfully set article $articleId accessible to $role"))
    }
}
