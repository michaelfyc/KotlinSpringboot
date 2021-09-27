package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.util.BadRequestException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.contracts.contract

@SpringBootTest
internal class ArticleServiceImplTest {

    private val existUserId = 9
    private val notExistUserId = 1

    private val title = "嘉然，你带我走吧"

    private val content = """
        猫猫还在害怕嘉然小姐。
        我会去把她爱的猫猫引来的。
        我知道稍有不慎，我就会葬身猫口。
        那时候嘉然小姐大概会把我的身体好好地装起来扔到门外吧。
        那我就成了一包鼠条，嘻嘻。
        我希望她能把我扔得近一点，因为我还是好喜欢她。会一直喜欢下去的。

        我的灵魂透过窗户向里面看去，挂着的铃铛在轻轻鸣响，嘉然小姐慵懒地靠在沙发上，表演得非常温顺的橘猫坐在她的肩膀。壁炉的火光照在她的脸庞，我冻僵的心脏在风里微微发烫。
    """.trimIndent()

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl

    lateinit var now: LocalDateTime

    lateinit var article: Article

    @BeforeEach
    fun prepare() {
        now = LocalDateTime.now().withNano(0)
        article = Article(title = title, authorId = existUserId, content = content, createAt = now)
    }

    @Test
    @Transactional
    fun testCreateArticle() {
        val articleId = articleServiceImpl.createArticle(article)
        val articleByArticleId = articleServiceImpl.getArticleByArticleId(articleId)
        assertNotNull(articleByArticleId)
        assertEquals(title, articleByArticleId!!.title)
        assertEquals(content, articleByArticleId.content)
        assertEquals(now, articleByArticleId.createAt.withNano(0))
    }

    @Test
    @Transactional
    fun testListArticleByAuthorId_0() {
        // 查询不存在的用户的文章
        assertThrows<BadRequestException> {
            val articles = articleServiceImpl.listArticleByAuthorId(notExistUserId)
            assertNull(articles)
        }
    }

    @Test
    @Transactional
    fun testListArticleByAuthorId_1() {
        val articleId = articleServiceImpl.createArticle(article)
        assertTrue(articleId > 0)
        // 查询存在的用户的文章
        val articles = articleServiceImpl.listArticleByAuthorId(existUserId)
        assertNotNull(articles)
        assertEquals(1, articles!!.size)
        val articleByAuthor = articles[0]
        assertEquals(article.title, articleByAuthor.title)
        assertEquals(article.content, articleByAuthor.content)
        assertEquals(article.createAt, articleByAuthor.createAt.withNano(0))
    }
}