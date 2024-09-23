package com.hola.glint.domain.image.application

import org.springframework.web.multipart.MultipartFile

interface FileUploadService {
    fun uploadProfileImageFile(filename: String, multipartFile: MultipartFile): String

    fun uploadAuthenticationImageFile(filename: String, multipartFile: MultipartFile): String

    fun getProfileImageFile(filename: String): ByteArray

    fun deleteProfileImageFile(filename: String)

    fun getFileUrl(objectKey: String): String
}