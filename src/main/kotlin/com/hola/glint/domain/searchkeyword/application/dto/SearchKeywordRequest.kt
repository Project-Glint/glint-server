package com.hola.glint.domain.searchkeyword.application.dto

import com.hola.glint.domain.searchkeyword.entity.SearchKeyword


data class SearchKeywordRequest(
    val keyword: String,
    val userId: Long
) {
    fun toEntity(): SearchKeyword {
        return SearchKeyword.createNew(userId, keyword)
    }
}
