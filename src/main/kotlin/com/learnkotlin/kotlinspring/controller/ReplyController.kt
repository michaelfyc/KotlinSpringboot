package com.learnkotlin.kotlinspring.controller

import com.github.pagehelper.PageHelper
import com.learnkotlin.kotlinspring.entity.Reply
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.service.impl.ReplyServiceImpl
import com.learnkotlin.kotlinspring.util.PagingUtil
import com.learnkotlin.kotlinspring.util.ResultVO
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
@RequestMapping("/api/reply")
class ReplyController {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private const val DEFAULT_PAGE_SIZE = 15
    }

    @Autowired
    private lateinit var replyServiceImpl: ReplyServiceImpl

    @PostMapping("/create")
    @NeedRole(CommonRoles.USER)
    fun createArticleReply(@Valid @RequestBody reply: Reply): ResultVO {
        logger.info("${reply.uid} creates a reply")
        val replyId = replyServiceImpl.createArticleReply(reply)
        return ResultVO(StatusOK(), mapOf("reply_id" to replyId))
    }

    @GetMapping("/")
    fun listRepliesOfArticle(
        @RequestBody
        @RequestParam("article_id") articleId: Int,
        @FromToken("role") role: CommonRoles?,
        @RequestParam(required = false, defaultValue = "1") pageNum: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE.toString()) pageSize: Int,
    ): ResultVO {
        val userRole = role ?: CommonRoles.GUEST
        PageHelper.startPage<Reply>(pageNum, pageSize)
        val replies = replyServiceImpl.listRepliesOfArticle(articleId, userRole)
        val repliesAfterPaging = PagingUtil.paging(replies, "replies")
        return ResultVO(StatusOK(), repliesAfterPaging)
    }

    @GetMapping
    fun listRepliesByUid(
        @RequestBody uid: Int,
        @FromToken("role") role: CommonRoles?,
        @RequestParam(required = false, defaultValue = "1") pageNum: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE.toString()) pageSize: Int
    ): ResultVO {
        val userRole = role ?: CommonRoles.GUEST
        PageHelper.startPage<Reply>(pageNum, pageSize)
        val replies = replyServiceImpl.listRepliesByUid(uid, userRole)
        val repliesAfterPaging = PagingUtil.paging(replies, "replies")
        return ResultVO(StatusOK(), repliesAfterPaging)
    }
}
