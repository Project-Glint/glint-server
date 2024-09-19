package com.hola.glint.domain.user.entity.enumerated

enum class SmokingType(
    val dbCode: SmokerTypeDbType,
) {
    NON_SMOKER(
        dbCode = SmokerTypeDbType.NON,
    ),
    TRYING_TO_QUIT(
        dbCode = SmokerTypeDbType.TTQ,
    ),
    OCCASIONAL(
        dbCode = SmokerTypeDbType.OCA,
    ),
    ONLY_WHEN_DRINKING(
        dbCode = SmokerTypeDbType.OWD,
    ),
    ELECTRONIC_CIGARETTE_USER(
        dbCode = SmokerTypeDbType.ECU,
    ),
    SMOKER(
        dbCode = SmokerTypeDbType.SMK,
    ),
    ;

    companion object {
        fun fromDbCode(dbCode: SmokerTypeDbType): SmokingType {
            return when (dbCode) {
                SmokerTypeDbType.NON -> NON_SMOKER
                SmokerTypeDbType.TTQ -> TRYING_TO_QUIT
                SmokerTypeDbType.OCA -> OCCASIONAL
                SmokerTypeDbType.OWD -> ONLY_WHEN_DRINKING
                SmokerTypeDbType.ECU -> ELECTRONIC_CIGARETTE_USER
                SmokerTypeDbType.SMK -> SMOKER
            }
        }

        enum class SmokerTypeDbType {
            NON,
            TTQ,
            OCA,
            OWD,
            ECU,
            SMK,
            ;
        }
    }
}
