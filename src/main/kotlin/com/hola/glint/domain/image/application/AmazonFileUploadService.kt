package com.hola.glint.domain.image.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import com.amazonaws.util.IOUtils
import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.system.error.FileNotFoundException
import io.netty.util.internal.StringUtil
import mu.KotlinLogging
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException

@Service
class AmazonFileUploadService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val s3Client: AmazonS3,
) : FileUploadService {
    override fun uploadProfileImageFile(filename: String, multipartFile: MultipartFile): String {
        return uploadFile(profileImagePreObjectKey, filename, multipartFile, true)
    }

    override fun uploadAuthenticationImageFile(filename: String, multipartFile: MultipartFile): String {
        return uploadFile(profileImagePreObjectKey, filename, multipartFile, true)
    }

    override fun getProfileImageFile(filename: String): ByteArray {
        return getFile(profileImagePreObjectKey + "/" + filename)
    }

    override fun deleteProfileImageFile(filename: String) {
        deleteFile(profileImagePreObjectKey, filename)
    }

    override fun getFileUrl(objectKey: String): String {
        if (!s3Client.doesObjectExist(bucket, objectKey)) {
            throw FileNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "not found s3Client object : $objectKey")
        }

        return s3Client.getUrl(bucket, objectKey).toString()
    }

    private fun uploadFile(
        preObjectkey: String,
        inputFileName: String,
        multipartFile: MultipartFile,
        needFullUrlWithEncoding: Boolean
    ): String {
        val fileName = if (StringUtil.isNullOrEmpty(inputFileName)) multipartFile.originalFilename else inputFileName

        val objectKey = "$preObjectkey/$fileName"

        try {
            val inputStream = multipartFile.inputStream
            val bytes = IOUtils.toByteArray(inputStream)

            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = bytes.size.toLong()

            val byteArrayInputStream = ByteArrayInputStream(bytes)

            s3Client.putObject(
                PutObjectRequest(bucket, objectKey, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: IOException) {
            logger.error("s3Client.putObject", e)
            throw RuntimeException("preObjectkey : " + preObjectkey + " | " + e.message)
        } catch (e: Exception) {
            logger.error("s3Client.putObject", e)
            throw RuntimeException("preObjectkey : " + preObjectkey + " | " + e.message)
        }

        return if (needFullUrlWithEncoding) s3Client.getUrl(bucket, objectKey).toString() else objectKey
    }

    private fun getFile(objectKey: String): ByteArray {
        try {
            val s3Object = s3Client!!.getObject(GetObjectRequest(bucket, objectKey))
            return IOUtils.toByteArray(s3Object.objectContent)
        } catch (ioe: IOException) {
            logger.error("s3Client.putObject", ioe)
            throw RuntimeException("objectkey : " + objectKey + " | " + ioe.message)
        } catch (e: Exception) {
            logger.error("s3Client.putObject", e)

            if (e is AmazonS3Exception && e.statusCode == 404) {
                throw FileNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "objectkey : " + objectKey + " | " + e.message)
            }

            throw RuntimeException("objectkey : " + objectKey + " | " + e.message)
        }
    }

    private fun deleteFile(preObjectkey: String, fileName: String) {
        val objectKey = "$preObjectkey/$fileName"

        s3Client.deleteObject(bucket, objectKey)
    }

    private fun deleteFiles(objectKeys: Array<String>) {
        val deleteObjectsRequest = DeleteObjectsRequest(bucket).withKeys(*objectKeys)

        s3Client.deleteObjects(deleteObjectsRequest)
    }

    companion object {
        var logger: Logger = KotlinLogging.logger {  }

        private const val profileImagePreObjectKey = "profile"
        private const val authenticationImagePreObjectKey = "authentication"
    }
}