package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ArticleMapper : BaseMapper<Article> {
    fun getArticleByArticleId(articleId: Int, role: CommonRoles): Article?

    fun listArticleByAuthorId(authorId: Int): List<Article>?

    fun setArticleAccessibleRole(articleId: Int, role: CommonRoles)

    fun listArticles(role: CommonRoles): List<Article>?
}
