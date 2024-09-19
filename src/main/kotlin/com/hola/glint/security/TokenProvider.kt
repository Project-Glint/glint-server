package com.hola.glint.security

import com.hola.glint.system.config.AppProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.Date
import java.util.UUID
import javax.crypto.spec.SecretKeySpec

@Service
class TokenProvider(
    private val appProperties: AppProperties,
) {
    private val log = KotlinLogging.logger { }
    private val secretKey = SecretKeySpec(Decoders.BASE64.decode(appProperties.auth.tokenSecret), SignatureAlgorithm.HS512.jcaName)

    fun createAccessToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + appProperties.auth.tokenExpirationMsec)
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun createRefreshToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + appProperties.auth.refreshTokenExpirationMsec)
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("value", UUID.randomUUID().toString())
            .signWith(secretKey)
            .compact()
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
        return claims.subject.toLong()
    }

    fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(authToken)
                .payload
            return true
        } catch (ex: SignatureException) {
            log.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            log.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            log.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            log.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            log.error("JWT claims string is empty.")
        }
        return false
    }

    fun parseToken(token: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
    }
}
