package com.hola.glint.domain.image.application

import com.hola.glint.domain.image.application.dto.ImageResponse
import com.hola.glint.domain.image.entity.ProfileImageFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val fileUploadService: FileUploadService
) {

    fun uploadProfileImageFile(imageFile: MultipartFile): ImageResponse {
        val profileImageFile: ProfileImageFile = ProfileImageFile.of(imageFile)

        return ImageResponse.of(
            profileImageFile.getUploadFileName(),
            fileUploadService.uploadProfileImageFile(
                profileImageFile.getUploadFileName(),
                profileImageFile.imageFile
            )
        )
    }

    fun uploadAuthenticationImageFile(imageFile: MultipartFile): ImageResponse {
        val profileImageFile: ProfileImageFile = ProfileImageFile.of(imageFile)

        return ImageResponse.of(
            profileImageFile.getUploadFileName(),
            fileUploadService.uploadAuthenticationImageFile(
                profileImageFile.getUploadFileName(),
                profileImageFile.imageFile
            )
        )
    }
}
