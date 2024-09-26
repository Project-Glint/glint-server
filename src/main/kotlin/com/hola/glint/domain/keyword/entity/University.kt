package com.hola.glint.domain.keyword.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "university")
class University(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    val id: Long? = null,

    @Column(name = "university_name")
    var universityName: String,

    @Column(name = "university_department")
    var universityDepartment: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_category_id")
    var universityCategory: UniversityCategory
) : BaseTimeEntity() {
    fun updateUniversity(universityName: String, universityDepartment: String, universityCategory: UniversityCategory) {
        this.universityName = universityName
        this.universityDepartment = universityDepartment
        this.universityCategory = universityCategory
    }

    companion object {
        fun createNewUniversity(
            universityName: String,
            universityDepartment: String,
            universityCategory: UniversityCategory,
        ) = University(
            universityName = universityName,
            universityDepartment = universityDepartment,
            universityCategory = universityCategory,
        )
    }
}