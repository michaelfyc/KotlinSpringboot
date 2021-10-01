package com.learnkotlin.kotlinspring.service.impl

import com.learnkotlin.kotlinspring.entity.Article
import com.learnkotlin.kotlinspring.exceptions.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
internal class ArticleServiceImplTest {

    private val existentAuthorId = 1
    private val nonexistentAuthorId = 2

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
        article = Article(title = title, authorId = existentAuthorId, content = content, createAt = now)
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
            val articles = articleServiceImpl.listArticleByAuthorId(nonexistentAuthorId)
            assertNull(articles)
        }
    }

    @Test
    @Transactional
    fun testListArticleByAuthorId_1() {
        // 查询存在的用户，但是没有创建文章
        val articles = articleServiceImpl.listArticleByAuthorId(existentAuthorId)
        assertNotNull(articles!!)
        assertEquals(0, articles.size)
    }

    @Test
    @Transactional
    fun testListArticleByAuthorId_2() {
        // 查询存在的用户，且用户有文章
        val articleId = articleServiceImpl.createArticle(article)
        assertTrue(articleId > 0)
        val articles = articleServiceImpl.listArticleByAuthorId(existentAuthorId)
        assertNotNull(articles)
        assertEquals(1, articles!!.size)
        val articleByAuthor = articles[0]
        assertEquals(article.title, articleByAuthor.title)
        assertEquals(article.content, articleByAuthor.content)
        assertEquals(article.createAt, articleByAuthor.createAt.withNano(0))
    }

    @Test
    @Transactional
    fun testGetArticleByArticleId_0() {
        // 查询不存在的 articleId
        val nonexistentArticleId = 999
        val article = articleServiceImpl.getArticleByArticleId(nonexistentArticleId)
        assertNull(article)
    }

    @Test
    @Transactional
    fun testGetArticleByArticleId_1() {
        // 查询存在的 articleId
        val articleId = articleServiceImpl.createArticle(article)
        val article = articleServiceImpl.getArticleByArticleId(articleId)
        assertNotNull(article)
        // 其余已在 testCreateArticle 验证过,省略
    }

    @Test
    @Transactional
    fun testSetVisibilityByAuthorId_0() {
        // 隐藏以后是看不到的
        val articleId = articleServiceImpl.createArticle(article)
        articleServiceImpl.setVisibilityByArticleId(articleId, false)
        val articleAfterUpdate = articleServiceImpl.getArticleByArticleId(articleId)
        assertNull(articleAfterUpdate)
    }

    @Test
    @Transactional
    fun testSetVisibilityByAuthorId_1() {
        // 解除隐藏就能看到
        article.isVisible = false
        val articleId = articleServiceImpl.createArticle(article)
        articleServiceImpl.setVisibilityByArticleId(articleId, true)
        val articleAfterUpdate = articleServiceImpl.getArticleByArticleId(articleId)
        assertNotNull(articleAfterUpdate)
        assertEquals(article.title, articleAfterUpdate?.title)
    }
}
