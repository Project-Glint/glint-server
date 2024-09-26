package com.hola.glint.domain.user.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.user.api.dto.UserInfoResponseDto
import com.hola.glint.domain.user.api.dto.UserProfileRequest
import com.hola.glint.domain.user.api.dto.UserProfileResponseDto
import com.hola.glint.domain.user.application.UserProfileService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserProfileController(
    private val userProfileService: UserProfileService
) {
    @Operation(summary = "Update or Create User Profile", description = "유저 프로필 수정 및 생성")
    @PutMapping(
        path = ["/{userId}/profile"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUserProfile(
        @PathVariable userId: Long,
        @RequestBody @Valid userProfileRequest: UserProfileRequest
    ): ResponseEntity<ResponseData<UserInfoResponseDto>> {
        return ResponseData.success(userProfileService.updateUserProfile(userId, userProfileRequest))
    }

    @Operation(summary = "Get User Profile", description = "User Id를 통한 User 프로필 조회")
    @GetMapping(path = ["/{userId}/profile"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserDetail(@PathVariable("userId") userId: Long): ResponseEntity<ResponseData<UserProfileResponseDto>> {
        return ResponseData.success(userProfileService.getUserProfileById(userId))
    }
}
