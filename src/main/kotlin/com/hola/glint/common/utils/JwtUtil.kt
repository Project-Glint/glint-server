package com.hola.glint.common.utils

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.system.error.GlintException
import com.hola.glint.system.error.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Component
class JwtUtil {
    @Value("\${app.auth.tokenSecret}")
    private lateinit var jwtPubKey: String

    fun getUserId(token: String): String? {
        return getClaims(token).subject
    }

    fun isExpiredToken(token: String): Boolean {
        return getClaims(token).expiration.before(Date())
    }

    fun getClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(generateJwtKeyDecryption())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        } catch (e: MalformedJwtException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        } catch (e: IllegalArgumentException) {
            throw UnauthorizedException(ErrorCode.UNAUTHORIZED)
        } catch (e: Exception) {
            throw GlintException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    private fun generateJwtKeyDecryption(): PublicKey {
        val keyFactory = KeyFactory.getInstance("RSA")
        val keyBytes = Base64.getDecoder().decode(jwtPubKey)
        val x509EncodedKeySpec = X509EncodedKeySpec(keyBytes)
        return keyFactory.generatePublic(x509EncodedKeySpec)
    }
}