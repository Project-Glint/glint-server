package com.hola.glint.domain.image.api

import com.hola.glint.domain.image.application.ImageService
import com.hola.glint.domain.image.application.dto.ImageResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/image")
class ImageController(
    private val imageService: ImageService
) {

    @Operation(summary = "uploadUserProfileImage", description = "유저 profile 사진 업로드")
    @PostMapping(
        value = ["/profile"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadUserProfileImage(@RequestPart imageFile: MultipartFile): ResponseEntity<ImageResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            imageService.uploadProfileImageFile(imageFile)
        )
    }

    @Operation(summary = "uploadUserAuthenticationImage", description = "유저 인증 자료 사진 업로드")
    @PostMapping(
        value = ["/authentication"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadUserAuthenticationImage(@RequestPart imageFile: MultipartFile): ResponseEntity<ImageResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            imageService.uploadAuthenticationImageFile(imageFile)
        )
    }
}