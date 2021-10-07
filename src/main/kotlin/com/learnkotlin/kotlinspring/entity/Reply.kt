package com.learnkotlin.kotlinspring.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.annotation.JsonFormat
import com.learnkotlin.kotlinspring.enums.CommonRoles
import org.apache.ibatis.annotations.AutomapConstructor
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@TableName("reply")
data class Reply @AutomapConstructor constructor(
    @TableId(type = IdType.AUTO)
    val id: Int = 0,
    var uid: Int,
    @TableField("article_id")
    val articleId: Int,
    @NotBlank
    var content: String,
    @TableField("reply_at")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss", timezone = "GMT+8")
    var replyAt: LocalDateTime = LocalDateTime.now(),
    @TableField("rid")
    var rid: Int = CommonRoles.GUEST.rid
)
