package com.hola.glint.domain.searchkeyword.application.dto

import com.hola.glint.domain.searchkeyword.entity.SearchKeyword
import java.time.format.DateTimeFormatter

data class SearchKeywordResponse(
    val id: Long,
    val keyword: String,
    val userId: Long?,
    val createdAt: String?
) {
    companion object {
        fun from(searchKeyword: SearchKeyword): SearchKeywordResponse {
            return SearchKeywordResponse(
                id = searchKeyword.searchKeywordId ?: throw IllegalArgumentException("ID cannot be null"),
                keyword = searchKeyword.keyword,
                userId = searchKeyword.userId,
                createdAt = searchKeyword.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }
    }
}
