package com.hola.glint.domain.user.api

import com.hola.glint.domain.user.application.UserService
import com.hola.glint.common.dto.ResponseData
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
//    private val userMeetingFacade: UserMeetingFacade
) {

    @Operation(summary = "Get user", description = "User Id를 통한 User 정보 조회")
    @GetMapping(value = ["/{userId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable("userId") userId: Long) =
        ResponseData.success(userService.getUserById(userId))


    @Operation(summary = "Get userInfo", description = "user, userDetail, userProfile 모든 정보 조회")
    @GetMapping(value = ["/{userId}/info"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserInfo(@PathVariable("userId") userId: Long) =
        ResponseData.success(userService.findUserInfoBy(userId))

    /*@Operation(summary = "Delete user", description = "User 삭제, 참가중인 미팅에서 모두 Out,참가신청 모두 거절")
    @DeleteMapping(value = ["/{userId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteUser(@PathVariable("userId") userId: Long?): ResponseEntity<*> {
        userMeetingFacade.deleteUser(userId)
        return ResponseEntity.noContent().build<Any>()
    }*/
}