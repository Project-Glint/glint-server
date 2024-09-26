package com.hola.glint.domain.keyword.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.user.entity.enumerated.DrinkingType
import com.hola.glint.domain.user.entity.enumerated.Religion
import com.hola.glint.domain.user.entity.enumerated.SmokingType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/keyword")
class KeywordController {
    @GetMapping("drinking")
    fun drinking() = ResponseData.success(DrinkingType.entries) // responseDto

    @GetMapping("religion")
    fun religion() = ResponseData.success(Religion.entries) // responseDto

    @GetMapping("smoking")
    fun smoking() = ResponseData.success(SmokingType.entries) // responseDto
}