package com.hola.glint.domain.auth.api

/*
import com.hola.glint.domain.user.api.dto.UserLoginResponseDto
import com.hola.glint.domain.user.api.dto.UserRequestDto
import com.hola.glint.domain.user.application.UserService
//import com.swyp.glint.common.authority.AuthorityHelper
//import com.swyp.glint.common.util.CookieUtil
//import com.swyp.glint.common.util.RedisUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
class AuthController(
    private val userService: UserService,
    private val socialOauthProvider: SocialOauthProvider,
//    private val authorityHelper: AuthorityHelper,
//    private val redisUtil: RedisUtil,
//    private val cookieUtil: CookieUtil,
) {
    @GetMapping(value = ["/auth/{socialType}"])
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        IOException::class
    )
    fun socialAuth(
        @PathVariable socialType: SocialType,
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse
    ) {
        val socialOauth = socialOauthProvider.getSocialOauth(socialType) as KakaoOauth

        try {
            httpServletResponse.sendRedirect(socialOauth.oauthRedirectURL)
        } catch (e: IOException) {
            throw IOException(e.message)
        }
    }


    @GetMapping("/auth/{socialType}/callback")
    @ResponseStatus(HttpStatus.OK)
    fun socialAuthCallBack(
        @PathVariable socialType: SocialType,
        @RequestParam(name = "code") code: String?,
        request: HttpServletRequest?,
        response: HttpServletResponse
    ): ResponseEntity<UserLoginResponseDto> {
        val socialOauth = socialOauthProvider.getSocialOauth(socialType) as KakaoOauth

        val oauthTokenResponse = socialOauth.requestAccessToken(code)
        val kakaoUserInfoResponse = socialOauth.getUserInfo(oauthTokenResponse.access_token)

        val userLoginResponse = userService.oauthLoginUser(
            UserRequestDto.of(kakaoUserInfoResponse.kakao_account.email, "ROLE_OAUTH_USER", SocialType.KAKAO.name)
        )

        refreshTokenHeader(response, userLoginResponse.email)
        return ResponseEntity.ok(userLoginResponse)
    }

    @PutMapping("/auth/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val accessToken = request.getHeader("Authorization")
        val refreshToken = request.getHeader("RefreshToken")

        response.setHeader("Authorization", "")
        response.setHeader("RefreshToken", "")

        val email = authorityHelper.getEmail(refreshToken.replace("Bearer ", ""))
        redisUtil.deleteData(email)
        return ResponseEntity.ok().build<Any>()
    }


    private fun refreshTokenHeader(response: HttpServletResponse, email: String) {
        val generateAccessToken = authorityHelper.generateToken(email)
        val generateRefreshToken = authorityHelper.generateToken(email)

        response.setHeader("Authorization", "Bearer $generateAccessToken")
        response.setHeader("RefreshToken", "Bearer $generateRefreshToken")

        redisUtil.setDataExpire(email, generateRefreshToken, AuthorityHelper.REFRESH_TOKEN_VALIDATION_SECOND)
    }

    private fun refreshTokenCookie(response: HttpServletResponse, email: String) {
        val generateAccessToken = authorityHelper.generateToken(email)
        val generateRefreshToken = authorityHelper.generateToken(email)

        val tokenCookie = cookieUtil.createAccessTokenCookie(generateAccessToken)
        val refreshTokenCookie = cookieUtil.createRefreshTokenCookie(generateRefreshToken)

        response.addCookie(tokenCookie)
        response.addCookie(refreshTokenCookie)

        redisUtil.setDataExpire(email, generateRefreshToken, AuthorityHelper.REFRESH_TOKEN_VALIDATION_SECOND)
    }
}*/
