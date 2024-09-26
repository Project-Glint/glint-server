package com.hola.glint.domain.keyword.repository

import com.hola.glint.domain.keyword.entity.WorkCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WorkCategoryRepository : JpaRepository<WorkCategory, Long> {
    // 인수로 받은 workName이 키워드와 일치했을 시 해당하는 WorkCategory 엔티티 List 반환
    @Query(
        """
        SELECT wc
        FROM WorkCategory wc
        JOIN wc.workCategoryKeywords kw
        WHERE :workName LIKE CONCAT('%', kw, '%')
        """
    )
    fun findByWorkNameContainingKeyword(@Param("workName") workName: String): WorkCategory?
}