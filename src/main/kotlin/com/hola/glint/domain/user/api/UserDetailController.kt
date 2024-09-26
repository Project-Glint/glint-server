package com.hola.glint.domain.user.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.user.api.dto.*
import com.hola.glint.domain.user.application.UserDetailService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/users")
class UserDetailController(
    private val userDetailService: UserDetailService
) {
    @Operation(summary = "Create User Detail", description = "새로운 User 추가 정보 생성", hidden = true)
    @PostMapping(
        path = ["/{userId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUserDetail(
        @PathVariable("userId") userId: Long,
        @RequestBody @Valid userDetailRequest: UserDetailRequestDto
    ): ResponseEntity<ResponseData<UserDetailResponseDto>> {
        return ResponseData.created(userDetailService.createUserDetail(userId, userDetailRequest))
    }

    @Operation(summary = "User NickName Validate Check", description = "닉네임 유효성 검사")
    @GetMapping(path = ["/nickname"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun userNickNameValidate(@RequestParam nickname: String): ResponseEntity<ResponseData<UserNickNameValidationResponseDto>> {
        return ResponseData.success(userDetailService.isNicknameTaken(nickname))
    }

    @Operation(
        summary = "User NickName Validate Check",
        description = "유저 닉네임 중복 체크, 닉네임 유효성 체크, 유효성 통과시 임시 UserDetail 생성 후 반환 (닉네임 선점)"
    )
    @PostMapping(
        path = ["/{userId}/nickname"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun userNickNameValidate(
        @PathVariable userId: Long,
        @RequestBody userNickNameRequestDto: UserNickNameRequestDto
    ): ResponseEntity<ResponseData<UserDetailResponseDto>> {
        return ResponseData.success(userDetailService.createTempUserDetail(userId, userNickNameRequestDto.nickname))
    }

    @Operation(summary = "Get User Detail", description = "User Id를 통한 User 추가 정보 조회")
    @GetMapping(path = ["/{userId}/detail"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserDetail(@PathVariable("userId") userId: Long): ResponseEntity<ResponseData<UserDetailResponseDto>> {
        return ResponseData.success(userDetailService.getUserDetailById(userId))
    }

    @Operation(summary = "Update user detail", description = "유저 추가 정보 생성 및 수정")
    @PutMapping(
        path = ["/{userId}/detail"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUserDetail(
        @PathVariable("userId") userId: Long,
        @RequestBody userDetailRequest: @Valid UserDetailRequestDto
    ): ResponseEntity<ResponseData<UserDetailResponseDto>> {
        return ResponseData.success(userDetailService.updateUserDetail(userId, userDetailRequest))
    }

    @Operation(summary = "userProfileImage 변경", description = "유저 프로필 이미지 변경")
    @PutMapping(
        path = ["/{userId}/profile-image"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUserProfileImage(
        @PathVariable userId: Long,
        @RequestPart imageFile: MultipartFile
    ): ResponseEntity<ResponseData<UserDetailResponseDto>> {
        return ResponseData.success(userDetailService.updateUserProfileImage(userId, imageFile))
    }
}
