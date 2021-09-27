package com.learnkotlin.kotlinspring.controller

import com.learnkotlin.kotlinspring.entity.Article
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController {
    fun listArticleByAuthorId(authorId: Int): Result<Article> {
        TODO("Not yet implemented")
    }
}