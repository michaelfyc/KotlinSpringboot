package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.ReplyRelation
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ReplyRelationMapper : BaseMapper<ReplyRelation> {
    fun createReplyToArticle(replyId: Int): Int

    fun createReplyToReply(replyId: Int, replyTo: Int): Int
}
