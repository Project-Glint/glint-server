package com.hola.glint.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap

@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseData<T>(

    @Schema(description = "status", example = "success")
    val status: String = "success",

    @Schema(description = "code", example = "200")
    val code: Int = 200,

    @Schema(description = "data")
    val data: T? = null,
) {
    companion object {

        // 200 OK(success): response data가 없을 때
        fun success() =
            ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    ResponseData(
                        status = "success",
                        code = 200,
                        data = null
                    )
                )

        // 200 OK(success): response data가 있을 때
        fun <T> success(data: T) =
            ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    ResponseData(
                        status = "success",
                        code = 200,
                        data = data
                    )
                )

        fun successResult(data: Boolean) =
            ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    ResponseData(
                        status = "success",
                        code = 200,
                        data = ResponseResult(data)
                    )
                )

        fun <T> successList(data: List<T>) =
            ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    ResponseData(
                        status = "success",
                        code = 200,
                        data = ResponseList(data)
                    )
                )

        // 200 OK(success): response header, data가 있을 때
        fun <T> success(
            header: MultiValueMap<String?, String?>,
            data: T
        ) =
            ResponseEntity
                .status(HttpStatus.OK)
                .header(header.toString())
                .body(
                    ResponseData(
                        status = "success",
                        code = 200,
                        data = data
                    )
                )
    }
}
