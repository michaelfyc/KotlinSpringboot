package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.Article
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ArticleMapper : BaseMapper<Article> {
    fun getArticleByArticleId(articleId: Int): Article?

    fun listArticleByAuthorId(authorId: Int): ArrayList<Article>?

    fun setVisibilityByArticleId(articleId: Int, visibility: Boolean)
}
