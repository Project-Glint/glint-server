package com.hola.glint.domain.user.entity.enumerated.converter

import com.hola.glint.domain.user.entity.enumerated.Religion
import com.hola.glint.domain.user.entity.enumerated.Religion.Companion.ReligionDbCode
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ReligionConverter : AttributeConverter<Religion, String> {
    override fun convertToDatabaseColumn(attribute: Religion?): String? {
        attribute ?: return null
        return attribute.dbCode.name
    }

    override fun convertToEntityAttribute(dbData: String?): Religion? {
        dbData ?: return null
        return Religion.fromDbCode(ReligionDbCode.valueOf(dbData))
    }
}
