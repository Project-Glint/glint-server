package com.hola.glint.domain.keyword.repository

import com.hola.glint.domain.keyword.entity.UniversityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface UniversityCategoryRepository : JpaRepository<UniversityCategory, Long> {
}