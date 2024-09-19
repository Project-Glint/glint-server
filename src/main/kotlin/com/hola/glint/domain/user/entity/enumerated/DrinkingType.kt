package com.hola.glint.domain.user.entity.enumerated

enum class DrinkingType(
    val dbCode: DrinkerTypeDbCode,
    val description: String,
) {
    NON_DRINKER(
        dbCode = DrinkerTypeDbCode.NON,
        description = "마시지 않음"
    ),
    ONLY_WHEN_REQUIRED(
        dbCode = DrinkerTypeDbCode.OWR,
        description = "어쩔 수 없을 때만",
    ),
    OCCASIONAL(
        dbCode = DrinkerTypeDbCode.OCC,
        description = "가끔",
    ),
    ENJOYS_DRINKING(
        dbCode = DrinkerTypeDbCode.ENJ,
        description = "어느 정도 즐김",
    ),
    LOVES_DRINKING(
        dbCode = DrinkerTypeDbCode.LOV,
        description = "좋아하는 편",
    ),
    WHALE(
        dbCode = DrinkerTypeDbCode.WHA,
        description = "술고래",
    ),
    ;

    companion object {
        fun fromDbCode(dbCode: DrinkerTypeDbCode): DrinkingType {
            return when (dbCode) {
                DrinkerTypeDbCode.NON -> NON_DRINKER
                DrinkerTypeDbCode.OWR -> ONLY_WHEN_REQUIRED
                DrinkerTypeDbCode.OCC -> OCCASIONAL
                DrinkerTypeDbCode.ENJ -> ENJOYS_DRINKING
                DrinkerTypeDbCode.LOV -> LOVES_DRINKING
                DrinkerTypeDbCode.WHA -> WHALE
            }
        }

        enum class DrinkerTypeDbCode {
            NON,
            OWR,
            OCC,
            ENJ,
            LOV,
            WHA,
            ;
        }
    }
}
