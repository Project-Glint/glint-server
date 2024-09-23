package com.hola.glint.domain.searchkeyword.api

import com.hola.glint.domain.searchkeyword.application.SearchKeywordService
import com.hola.glint.domain.searchkeyword.application.dto.SearchKeywordRequest
import com.hola.glint.domain.searchkeyword.application.dto.SearchKeywordResponse
import com.hola.glint.domain.searchkeyword.application.dto.SearchKeywordResponses
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SearchKeywordController(
    private val searchKeywordService: SearchKeywordService
) {

    @Operation(summary = "검색 키워드 저장", description = "검색 키워드 저장, 미팅 검색때 자동으로 들어갑니다. 따로 api를 호출할 필요는 없습니다!")
    @PostMapping("/search-keywords")
    fun createSearchKeyword(@RequestBody searchKeywordRequest: SearchKeywordRequest): ResponseEntity<SearchKeywordResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            searchKeywordService.saveSearchKeyword(
                searchKeywordRequest.keyword,
                searchKeywordRequest.userId
            )
        )
    }

    @Operation(summary = "최근 검색 키워드 조회", description = "최근 검색 키워드 조회, limit 안넣을시 기본 5개")
    @GetMapping("/search-keywords/users/{userId}")
    fun getRecentSearchKeywords(
        @PathVariable userId: Long,
        @RequestParam(required = false) limit: Int?
    ): ResponseEntity<SearchKeywordResponses> {
        return ResponseEntity.ok(
            searchKeywordService.getRecentSearchKeywords(userId, limit)
        )
    }

    @Operation(summary = "최근 검색 키워드 삭제", description = "검색 키워드 삭제")
    @DeleteMapping("/search-keywords/{searchKeywordId}")
    fun removeRecentSearchKeywords(@PathVariable searchKeywordId: Long): ResponseEntity<Void> {
        searchKeywordService.removeRecentSearchKeywords(searchKeywordId)
        return ResponseEntity.noContent().build()
    }
}
