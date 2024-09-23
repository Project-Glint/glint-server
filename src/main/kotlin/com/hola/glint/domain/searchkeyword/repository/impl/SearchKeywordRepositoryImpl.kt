package com.hola.glint.domain.searchkeyword.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import com.hola.glint.domain.searchkeyword.entity.SearchKeyword
import com.hola.glint.domain.searchkeyword.repository.SearchKeywordRepositoryCustom
import org.springframework.stereotype.Repository

import com.hola.glint.domain.searchkeyword.entity.QSearchKeyword.searchKeyword

@Repository
class SearchKeywordRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SearchKeywordRepositoryCustom {

    override fun findAllByUserId(userId: Long, limit: Int): List<SearchKeyword> {
        return queryFactory
            .select(searchKeyword)
            .from(searchKeyword)
            .where(
                searchKeyword.userId.eq(userId),
                searchKeyword.archived.isFalse
            )
            .groupBy(searchKeyword.keyword)
            .orderBy(searchKeyword.searchKeywordId.desc())
            .limit(limit.toLong())
            .fetch()
    }
}
