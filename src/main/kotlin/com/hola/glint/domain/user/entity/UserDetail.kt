package com.hola.glint.domain.user.entity

import com.hola.glint.domain.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.Period
import java.util.*

@Entity
@Table(name = "user_detail")
class UserDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(
        name = "user_id",
        nullable = false,
        unique = true
    )
    var userId: Long,

    @Column(
        name = "nickname",
        unique = true
    )
    var nickname: String,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "birthdate")
    var birthdate: LocalDate? = null,

    @Column(name = "height")
    var height: Int,

    @Column(name = "profile_image")
    var profileImage: String? = null

) : BaseTimeEntity() {

    fun updateNickname(nickname: String) {
        this.nickname = nickname
    }

    fun updateUserDetail(nickname: String, gender: String, birthdate: LocalDate?, height: Int, profileImage: String) {
        this.nickname = nickname
        this.gender = gender
        this.birthdate = birthdate
        this.height = height
        this.profileImage = profileImage
    }

    fun calculateAgeByBirthdate(): Int {
        if (birthdate == null) {
            return 0
            //throw new IllegalArgumentException("Birthdate can not be null");
        }
        return Period.between(birthdate, LocalDate.now()).years
    }

    fun isSameGender(gender: String): Boolean {
        return this.gender == gender
    }

    fun isAgeIn(minAge: Int, maxAge: Int): Boolean {
        val age: Int = calculateAge(this.birthdate)
        return age in minAge..maxAge
    }

    fun isHeightIn(maxHeight: Int, minHeight: Int): Boolean {
        return height in minHeight..maxHeight
    }

    val isFemale: Boolean get() = gender == "FEMALE"
    val isMale: Boolean get() = gender == "MALE"

    fun updateProfileUrl(userProfileImageUrl: String) {
        this.profileImage = userProfileImageUrl
    }

    val isComplete: Boolean
        get() = Objects.nonNull(nickname) &&
                Objects.nonNull(gender) &&
                Objects.nonNull(birthdate) &&
                Objects.nonNull(height) &&
                Objects.nonNull(profileImage)

    companion object {
        fun createNewUserDetail(
            userId: Long,
            nickname: String,
            gender: String,
            birthdate: LocalDate,
            height: Int,
            profileImage: String
        ) = UserDetail (
                userId = userId,
                nickname = nickname,
                gender = gender,
                birthdate = birthdate,
                height = height,
                profileImage = profileImage,
        )

        /*fun createTempUserDetailByNickName(userId: Long) =
            UserDetail(
                userId = userId,
                nickname = TODO(),
                gender = TODO(),
                birthdate = TODO(),
                height = TODO(),
                profileImage = TODO()
            )*/

        fun calculateAge(birthDate: LocalDate?): Int {
            return Period.between(birthDate, LocalDate.now()).years
        }
    }
}