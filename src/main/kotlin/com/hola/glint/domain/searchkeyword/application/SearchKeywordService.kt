package com.hola.glint.domain.searchkeyword.application

import com.hola.glint.domain.searchkeyword.application.dto.SearchKeywordResponse
import com.hola.glint.domain.searchkeyword.application.dto.SearchKeywordResponses
import com.hola.glint.domain.searchkeyword.entity.SearchKeyword
import com.hola.glint.domain.searchkeyword.repository.SearchKeywordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class SearchKeywordService(
    private val searchKeywordRepository: SearchKeywordRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveSearchKeyword(keyword: String, userId: Long): SearchKeywordResponse {
        searchKeywordRepository.findByKeyword(keyword, userId)
            .forEach { it.archive() }

        return SearchKeywordResponse.from(searchKeywordRepository.save(SearchKeyword.createNew(userId, keyword)))
    }

    fun getRecentSearchKeywords(userId: Long, limit: Int?): SearchKeywordResponses {
        val finalLimit = limit ?: 5
        return SearchKeywordResponses.from(
            searchKeywordRepository.findAllByUserId(userId, finalLimit)
        )
    }

    @Transactional
    fun removeRecentSearchKeywords(searchKeywordId: Long) {
        val searchKeywordOptional = searchKeywordRepository.findById(searchKeywordId)
        searchKeywordOptional.ifPresent { it.archive() }
    }
}
