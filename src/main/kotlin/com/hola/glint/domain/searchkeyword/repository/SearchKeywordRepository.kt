package com.hola.glint.domain.searchkeyword.repository

import com.hola.glint.domain.searchkeyword.entity.SearchKeyword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface SearchKeywordRepository : JpaRepository<SearchKeyword, Long>, SearchKeywordRepositoryCustom {

    @Query("""
        SELECT sk
        FROM SearchKeyword sk
        WHERE sk.userId = :userId
        AND sk.keyword = :keyword
        AND sk.archived = false        
    """)
    fun findByKeyword(keyword: String, userId: Long): List<SearchKeyword>

    @Query("""
        SELECT sk
        FROM SearchKeyword sk
        WHERE sk.userId = :userId
        AND sk.searchKeywordId = :searchKeywordId
        AND sk.archived = false        
    """)
    fun findByUserIdAndSearchKeywordId(userId: Long, searchKeywordId: Long): Optional<SearchKeyword>
}
