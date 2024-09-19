package com.hola.glint.domain.user.entity.enumerated.converter

import com.hola.glint.domain.user.entity.enumerated.DrinkingType
import com.hola.glint.domain.user.entity.enumerated.DrinkingType.Companion.DrinkerTypeDbCode
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class DrinkingTypeConverter : AttributeConverter<DrinkingType, String> {
    override fun convertToDatabaseColumn(attribute: DrinkingType?): String? {
        attribute ?: return null
        return attribute.dbCode.name
    }

    override fun convertToEntityAttribute(dbData: String?): DrinkingType? {
        dbData ?: return null
        return DrinkingType.fromDbCode(DrinkerTypeDbCode.valueOf(dbData))
    }
}
