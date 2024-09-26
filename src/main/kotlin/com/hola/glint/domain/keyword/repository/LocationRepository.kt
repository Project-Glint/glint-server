package com.hola.glint.domain.keyword.repository

import com.hola.glint.domain.keyword.entity.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location, Long> {
    fun findByStateAndCity(state: String, city: String): Location
    fun findAllCityByState(state: String?): List<Location>
    fun findStateByCity(city: String): Location?
}