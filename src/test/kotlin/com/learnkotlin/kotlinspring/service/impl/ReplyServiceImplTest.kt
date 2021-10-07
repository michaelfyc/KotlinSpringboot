package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.entity.Reply
import com.learnkotlin.kotlinspring.entity.User
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class ReplyServiceImplTest {

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var replyServiceImpl: ReplyServiceImpl

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    private lateinit var article: Article

    private lateinit var replyToArticle: Reply

    private lateinit var replyToReply: Reply

    private var articleId = 0

    @BeforeEach
    fun setup() {
        // create valid user
        val user = User(username = "David", password = "321456", email = "david@example.com")
        val userId = userServiceImpl.createUser(user)
        val anotherUser = User(username = "Kate", password = "qwertyu", email = "kate@example.com")
        val anotherUserId = userServiceImpl.createUser(anotherUser, CommonRoles.ADMIN)

        // create valid article
        article = Article(title = "嘉然", content = "你带我走吧", authorId = userId)
        articleId = articleServiceImpl.createArticle(article)

        // create valid replies
        replyToArticle = Reply(uid = userId, content = "好好好", articleId = articleId)
        replyToReply = Reply(uid = anotherUserId, content = "水军是吧", articleId = articleId)
    }

    @Test
    @Transactional
    fun testCreateArticleReply_0() {
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        val reply = replyServiceImpl.getReplyByReplyId(replyId)
        assertNotNull(reply)
        assertEquals(replyToArticle.articleId, reply!!.articleId)
        assertEquals(replyToArticle.uid, reply.uid)
        assertEquals(replyToArticle.content, reply.content)
        assertEquals(replyToArticle.replyAt.withNano(0), reply.replyAt.withNano(0))
    }

    @Test
    @Transactional
    fun testCreateReplyToReply_0() {
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        val reply2ReplyId = replyServiceImpl.createReplyToReply(replyToReply, replyId)
        assertTrue(reply2ReplyId > 0)
        val replies = replyServiceImpl.listRepliesOfReply(replyId)
        assertEquals(1, replies.size)
    }

    @Test
    fun testGetReplyByReplyId_0() {
        // select a nonexistent reply should return null
        val reply = replyServiceImpl.getReplyByReplyId(999)
        assertNull(reply)
    }

    @Test
    fun testReplyByReplyId_1() {
        // test get reply to reply
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        val reply2ReplyId = replyServiceImpl.createReplyToReply(replyToReply, replyId)
        assertTrue(reply2ReplyId > 0)
        val reply2Reply = replyServiceImpl.getReplyByReplyId(reply2ReplyId)
        assertNotNull(reply2Reply)
        assertEquals(replyToReply.content, reply2Reply!!.content)
        assertEquals(replyToReply.uid, reply2Reply.uid)
        assertEquals(replyToReply.articleId, reply2Reply.articleId)
        assertEquals(replyToReply.replyAt.withNano(0), reply2Reply.replyAt.withNano(0))
        assertEquals(CommonRoles.GUEST.rid, reply2Reply.rid)
    }

    @Test
    @Transactional
    fun testListRepliesOfArticle_0() {
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        val replies = replyServiceImpl.listRepliesOfArticle(articleId)
        assertEquals(1, replies.size)
        assertEquals(replyId, replies[0].id)
        assertEquals(replyToArticle.uid, replies[0].uid)
        assertEquals(replyToArticle.content, replies[0].content)
        assertEquals(CommonRoles.GUEST.rid, replies[0].rid)
        assertEquals(replyToArticle.replyAt.withNano(0), replies[0].replyAt.withNano(0))
    }

    @Test
    @Transactional
    fun testListRepliesByUid_0() {
        // test replies of different articles
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        val anotherArticle = Article(title = "拉姐", content = "mua 你一下", authorId = article.authorId)
        val anotherArticleId = articleServiceImpl.createArticle(anotherArticle)
        assertTrue(anotherArticleId > 0)
        val replyToAnotherArticle = Reply(uid = replyToArticle.uid, content = "不好不好不好", articleId = anotherArticleId)
        val replyIdToAnotherArticle = replyServiceImpl.createArticleReply(replyToAnotherArticle)
        assertTrue(replyIdToAnotherArticle > 0)

        val replies = replyServiceImpl.listRepliesByUid(article.authorId)
        assertEquals(2, replies.size)
        assertEquals(replyToArticle.content, replies[0].content)
        assertEquals(replyToArticle.articleId, replies[0].articleId)
        assertEquals(replyToArticle.replyAt.withNano(0), replies[0].replyAt.withNano(0))
    }

    @Test
    @Transactional
    fun testListRepliesByUid_1() {
        // test replies to article and another reply
        val replyId = replyServiceImpl.createArticleReply(replyToArticle)
        assertTrue(replyId > 0)
        replyToReply.uid = replyToArticle.uid
        val reply2ReplyId = replyServiceImpl.createReplyToReply(replyToReply, replyId)
        assertTrue(reply2ReplyId > 0)

        val replies = replyServiceImpl.listRepliesByUid(replyToArticle.uid)
        assertEquals(2, replies.size)
    }
}
