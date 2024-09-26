package com.hola.glint.domain.keyword.repository

import com.hola.glint.domain.keyword.entity.University
import org.springframework.data.jpa.repository.JpaRepository

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByUniversityNameAndUniversityDepartment(universityName: String, universityDepartment: String): University

    fun findByUniversityName(universityName: String): List<University>
}