package com.learnkotlin.kotlinspring.service

import com.learnkotlin.kotlinspring.entity.Reply
import com.learnkotlin.kotlinspring.enums.CommonRoles

interface IReplyService {
    fun createArticleReply(reply: Reply): Int

    fun createReplyToReply(reply: Reply, replyTo: Int): Int

    fun getReplyByReplyId(id: Int, role: CommonRoles = CommonRoles.GUEST): Reply?

    fun listRepliesOfArticle(articleId: Int, role: CommonRoles = CommonRoles.GUEST): List<Reply>

    fun listRepliesOfReply(replyId: Int, role: CommonRoles = CommonRoles.GUEST): List<Reply>

    fun listRepliesByUid(uid: Int, role: CommonRoles = CommonRoles.GUEST): List<Reply>
}
