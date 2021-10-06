package com.learnkotlin.kotlinspring.util

import com.github.pagehelper.PageInfo

object PagingUtil {
    fun <T> paging(list: List<T>, dataName: String): Map<String, Any> {
        val listAfterPaging = PageInfo(list)
        return mapOf(
            dataName to listAfterPaging.list,
            "current_page" to listAfterPaging.pageNum,
            "total" to listAfterPaging.total,
            "has_next_page" to listAfterPaging.isHasNextPage,
            "has_previous_page" to listAfterPaging.isHasPreviousPage
        )
    }
}
