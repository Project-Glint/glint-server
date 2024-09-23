package com.hola.glint.domain.searchkeyword.repository

import com.hola.glint.domain.searchkeyword.entity.SearchKeyword

interface SearchKeywordRepositoryCustom {
    fun findAllByUserId(userId: Long, limit: Int): List<SearchKeyword>
}
