package com.hola.glint.domain.keyword.repository

import com.hola.glint.domain.keyword.entity.Work
import org.springframework.data.jpa.repository.JpaRepository

interface WorkRepository : JpaRepository<Work, Long> {
    fun findByWorkName( workName: String): Work?
}