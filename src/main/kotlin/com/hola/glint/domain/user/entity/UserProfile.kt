package com.hola.glint.domain.user.entity

import com.hola.glint.domain.BaseTimeEntity
import com.hola.glint.domain.user.entity.enumerated.DrinkingType
import com.hola.glint.domain.user.entity.enumerated.Religion
import com.hola.glint.domain.user.entity.enumerated.SmokingType
import jakarta.persistence.*

@Entity
@Table(name = "user_profile")
// 회사 or 학교, 위치, 자기소개, 키워드(위치, 종교, 흡연여부, 음주여부), 자유 태그("적극적", "ENTJ", "러닝")
class UserProfile(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,

    @Column(
        name = "user_id",
        nullable = false,
        unique = true
    )
    var userId: Long,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "work_id")
//    var work: Work,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "university_id")
//    var university: University,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id")
//    var location: Location,

    @Column(name = "religion", length = 10, nullable = false)
    var religion: Religion,

    @Column(name = "smoking_type", length = 25, nullable = false)
    var smokingType: SmokingType,

    @Column(name = "drinking_type", length = 18, nullable = false)
    var drinkingType: DrinkingType,

    @Column(name = "self_introduction", length = 1000, nullable = false)
    var selfIntroduction: String,

    @ElementCollection
    @CollectionTable(name = "user_profile_hashtags", joinColumns = [JoinColumn(name = "user_profile_id")])
    @Column(name = "hashtag")
    var hashtags: List<String>?
) : BaseTimeEntity() {

    /*val affiliation: String?
        get() {
            if (Objects.nonNull(work)) {
                return work.getWorkName()
            }

            if (Objects.nonNull(university)) {
                return university.getUniversityName()
            }

            return null
        }*/

    companion object {
        fun createNewUserProfile(
            userId: Long,
//            work: Work?,
//            university: University?,
//            location: Location?,
            religion: Religion,
            smokingType: SmokingType,
            drinkingType: DrinkingType,
            selfIntroduction: String,
            hashtags: List<String>?
        ) = UserProfile(
            userId = userId,
            religion = religion,
            smokingType = smokingType,
            drinkingType = drinkingType,
            hashtags = hashtags,
            selfIntroduction = selfIntroduction
        )
    }
}
