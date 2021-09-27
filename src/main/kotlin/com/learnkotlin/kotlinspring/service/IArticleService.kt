package com.learnkotlin.kotlinspring.service

import com.learnkotlin.kotlinspring.entity.Article

interface IArticleService {
    fun listArticleByAuthorId(authorId: Int): ArrayList<Article>?

    /**
     * 新建帖子，并返回帖子 id
     */
    fun createArticle(article: Article): Int

    /**
     * 由帖子 id 获取帖子
     */
    fun getArticleByArticleId(articleId: Int): Article?
}
