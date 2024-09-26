package com.hola.glint.domain.keyword.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.annotation.PersistenceCreator

@Entity
@Table(name = "university_category")
class UniversityCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_category_id")
    val id: Long = 0,

    @Column(
        name = "university_category_name"
    )
    val universityCategoryName: String
) : BaseTimeEntity()