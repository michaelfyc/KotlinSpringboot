package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Reply
import com.learnkotlin.kotlinspring.enums.CommonRoles
import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import com.learnkotlin.kotlinspring.mapper.ReplyMapper
import com.learnkotlin.kotlinspring.mapper.ReplyRelationMapper
import com.learnkotlin.kotlinspring.service.IReplyService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReplyServiceImpl : IReplyService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var replyMapper: ReplyMapper

    @Autowired
    private lateinit var replyRelationMapper: ReplyRelationMapper

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    @Transactional
    override fun createArticleReply(reply: Reply): Int {
        // first, see if article exists
        articleServiceImpl.getArticleByArticleId(reply.articleId)
            ?: throw BadRequestException(message = "article does not exist")
        // then, create a record in table `reply`
        replyMapper.insert(reply)
        // lastly, store a self-referenced parent-children relation in table `reply_relation`
        replyRelationMapper.createReplyToArticle(reply.id)
        logger.info("<ReplyServiceImpl.createArticleReply>user ${reply.uid} replies to ${reply.articleId}")
        return reply.id
    }

    @Transactional
    override fun createReplyToReply(reply: Reply, replyTo: Int): Int {
        // first, see if article exists
        articleServiceImpl.getArticleByArticleId(reply.articleId)
            ?: throw BadRequestException(message = "article does not exist")
        // then, create a record in table `reply`
        replyMapper.insert(reply)
        // lastly, store the reply relation between current reply and that it replies to in table `reply_relation`
        replyRelationMapper.createReplyToReply(reply.id, replyTo)
        logger.info("<ReplyServiceImpl.createReplyToReply> user ${reply.uid} replies to $replyTo")
        return reply.id
    }

    override fun getReplyByReplyId(id: Int, role: CommonRoles): Reply? {
        val reply = replyMapper.getReplyByReplyId(id, role)
        return reply
    }

    override fun listRepliesOfArticle(articleId: Int, role: CommonRoles): List<Reply> {
        val replies = replyMapper.listRepliesOfArticle(articleId, role)
        return replies ?: emptyList()
    }

    override fun listRepliesOfReply(replyId: Int, role: CommonRoles): List<Reply> {
        val replies = replyMapper.listRepliesOfReply(replyId, role)
        return replies ?: emptyList()
    }

    override fun listRepliesByUid(uid: Int, role: CommonRoles): List<Reply> {
        val replies = replyMapper.listRepliesByUid(uid, role)
        return replies ?: emptyList()
    }
}
