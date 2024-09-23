package com.hola.glint.domain.image.application.dto

data class ImageResponse(
    val filename: String,
    val url: String
) {
    companion object {
        fun of(filename: String, url: String): ImageResponse {
            return ImageResponse(filename, url)
        }
    }
}
