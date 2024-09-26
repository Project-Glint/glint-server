package com.hola.glint.domain.keyword.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "work")
class Work(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    val id: Long? = null,

    @Column(name = "work_name")
    var workName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_category_id")
    var workCategory: WorkCategory?
) : BaseTimeEntity() {

    fun updateWork(workName: String, workCategory: WorkCategory?) {
        this.workName = workName
        this.workCategory = workCategory
    }

    companion object {
        fun createNewWork(workName: String) =
            Work(
                workName = workName,
                workCategory = null,
            )
    }
}