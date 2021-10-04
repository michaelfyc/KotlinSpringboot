package com.learnkotlin.kotlinspring.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.annotation.JsonFormat
import org.apache.ibatis.annotations.AutomapConstructor
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@TableName("article")
data class Article @AutomapConstructor constructor(
    @TableId(value = "article_id", type = IdType.AUTO)
    val articleId: Int = 0,
    @NotBlank
    var title: String,
    @NotBlank
    var content: String,
    @TableField("author_id")
    var authorId: Int,
    @TableField("create_at")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss", timezone = "GMT+8")
    val createAt: LocalDateTime = LocalDateTime.now(),
    @TableField("rid")
    var rid: Int = 4
)
