package com.hola.glint.domain.image.entity

import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*
import java.util.stream.Collectors

class ProfileImageFile(
    val imageFile: MultipartFile,
) {
    fun getUploadFileName(): String {
        return getFileName(Objects.requireNonNull(imageFile.originalFilename));
    }

    private fun getFileName(imageFilePath: String?): String {
        val filenames =
            Arrays.stream(imageFilePath!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).collect(
                Collectors.toList()
            )
        val unixTime = Instant.now().epochSecond

        return "profile" + "_" + unixTime + "." + getSuffix(filenames)
    }

    private fun getSuffix(fileNames: List<String>): String {
        val suffix = fileNames[fileNames.size - 1]
        if ("jpg" == suffix) {
            return "jpg"
        } else if ("png" == suffix) {
            return "png"
        }

        return "jpg"
    }

    companion object {
        fun of(imageFile: MultipartFile): ProfileImageFile {
            return ProfileImageFile(imageFile)
        }
    }
}
