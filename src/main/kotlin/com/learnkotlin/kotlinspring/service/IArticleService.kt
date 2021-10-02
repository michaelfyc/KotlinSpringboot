package com.learnkotlin.kotlinspring.service

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.enums.CommonRoles

interface IArticleService {
    /**
     * 显示某用户的所有帖子
     */
    fun listArticleByAuthorId(authorId: Int): List<Article>

    /**
     * 新建帖子，并返回帖子 id
     */
    fun createArticle(article: Article): Int

    /**
     * 由帖子 id 获取帖子
     */
    fun getArticleByArticleId(articleId: Int, role: CommonRoles = CommonRoles.GUEST): Article?

    /**
     * 设置帖子只能被特定角色的权限访问
     */
    fun setArticleAccessibleRole(articleId: Int, role: CommonRoles)

    /**
     * 获取特定角色所有能看到的帖子
     */
    fun listArticles(role: CommonRoles): List<Article>
}
