package com.hola.glint.domain.searchkeyword.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "search_keyword")
class SearchKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_keyword_id")
    val searchKeywordId: Long? = null,

    @Column(name = "keyword", nullable = false)
    val keyword: String,

    @Column(name = "user_id", nullable = true)
    val userId: Long? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "archived", nullable = false)
    var archived: Boolean = false
) {

    fun archive() {
        this.archived = true
    }

    companion object {
        fun createNew(userId: Long?, keyword: String): SearchKeyword {
            return SearchKeyword(
                keyword = keyword,
                userId = userId,
                createdAt = LocalDateTime.now(),
                archived = false
            )
        }
    }
}
