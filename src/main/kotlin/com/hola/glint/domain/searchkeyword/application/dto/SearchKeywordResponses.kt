package com.hola.glint.domain.searchkeyword.application.dto

import com.hola.glint.domain.searchkeyword.entity.SearchKeyword

data class SearchKeywordResponses(
    val searchKeywords: List<SearchKeywordResponse>
) {
    companion object {
        fun from(searchKeywords: List<SearchKeyword>): SearchKeywordResponses {
            return SearchKeywordResponses(
                searchKeywords.map { SearchKeywordResponse.from(it) }
            )
        }
    }
}
