package com.hola.glint.domain.keyword.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "location")
@Entity
class Location(
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id val id: Long? = null,

    @Column(name = "state")
    var state: String, // 시, 도

    @Column(name = "city")
    var city: String, // 시, 군, 구
) : BaseTimeEntity() {
    fun updateLocation(state: String, city: String) {
        this.state = state
        this.city = city
    }

    companion object {
        fun createNewLocation(state: String, city: String): Location {
            return Location(
                state = state,
                city = city,
            )
        }
    }
}
