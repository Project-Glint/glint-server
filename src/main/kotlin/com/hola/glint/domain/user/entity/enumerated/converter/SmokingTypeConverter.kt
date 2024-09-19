package com.hola.glint.domain.user.entity.enumerated.converter

import com.hola.glint.domain.user.entity.enumerated.SmokingType
import com.hola.glint.domain.user.entity.enumerated.SmokingType.Companion.SmokerTypeDbType
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class SmokingTypeConverter : AttributeConverter<SmokingType, String> {
    override fun convertToDatabaseColumn(attribute: SmokingType?): String? {
        attribute ?: return null
        return attribute.dbCode.name
    }

    override fun convertToEntityAttribute(dbData: String?): SmokingType? {
        dbData ?: return null
        return SmokingType.fromDbCode(SmokerTypeDbType.valueOf(dbData))
    }
}
