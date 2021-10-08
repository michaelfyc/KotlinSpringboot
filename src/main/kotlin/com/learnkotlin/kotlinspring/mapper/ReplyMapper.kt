package com.learnkotlin.kotlinspring.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.learnkotlin.kotlinspring.entity.Reply
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ReplyMapper : BaseMapper<Reply> {
    fun listRepliesOfArticle(articleId: Int, role: CommonRoles): List<Reply>?

    fun listRepliesByUid(uid: Int, role: CommonRoles): List<Reply>?

    fun listRepliesOfReply(replyId: Int, role: CommonRoles): List<Reply>?

    fun getReplyByReplyId(replyId: Int, role: CommonRoles): Reply?
}
