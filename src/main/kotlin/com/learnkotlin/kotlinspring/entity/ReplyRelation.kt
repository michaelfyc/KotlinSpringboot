package com.learnkotlin.kotlinspring.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.apache.ibatis.annotations.AutomapConstructor
import javax.validation.constraints.Min

@TableName("reply_relation")
data class ReplyRelation @AutomapConstructor constructor(
    @TableId(type = IdType.AUTO)
    val id: Int,
    @TableField("reply_id")
    @field:Min(1)
    val replyId: Int,
    @TableField("reply_to")
    @field:Min(1)
    val replyTo: Int
)
