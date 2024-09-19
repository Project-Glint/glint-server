package com.hola.glint.domain.user.entity.enumerated

enum class Religion(
    val dbCode: ReligionDbCode,
) {
    NONE(
        dbCode = ReligionDbCode.NON,
    ),
    PROTESTANT(
        dbCode = ReligionDbCode.PTT
    ),
    CATHOLIC(
        dbCode = ReligionDbCode.CAT
    ),
    BUDDHIST(
        dbCode = ReligionDbCode.BUD
    ),
    OTHER(
        dbCode = ReligionDbCode.OTH
    ),
    ;

    companion object {
        fun fromDbCode(dbCode: ReligionDbCode): Religion {
            return when (dbCode) {
                ReligionDbCode.NON -> NONE
                ReligionDbCode.PTT -> PROTESTANT
                ReligionDbCode.CAT -> CATHOLIC
                ReligionDbCode.BUD -> BUDDHIST
                ReligionDbCode.OTH -> OTHER
            }
        }
        enum class ReligionDbCode {
            NON,
            PTT,
            CAT,
            BUD,
            OTH,
            ;
        }
    }
}
