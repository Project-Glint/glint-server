package com.hola.glint.domain.keyword.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "work_category")
class WorkCategory(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "work_category_id")
    val id: Long? = null, // 의사, 삼성, 한국전력공사, 7급

    @Column(name = "keyword")
    @CollectionTable(
        name = "work_category_keywords",
        joinColumns = [JoinColumn(name = "work_category_id")]
    )
    @ElementCollection
    val workCategoryKeywords: List<String>,

    @Column(name = "work_category_name")
    val workCategoryName: String,
) : BaseTimeEntity()