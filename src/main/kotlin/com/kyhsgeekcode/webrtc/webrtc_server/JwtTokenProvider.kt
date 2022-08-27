package com.kyhsgeekcode.webrtc.webrtc_server

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider(private val userDetailsService: UserDetailsService) {
    @Value("\${jwt.token.secret-key}")
    private val secret_key: String? = null

    @Value("\${jwt.token.expire-length}")
    private val expire_time: Long = 0

    /**
     * 적절한 설정을 통해 토큰을 생성하여 반환
     * @param authentication
     * @return
     */
    fun generateToken(authentication: Authentication): String {
        val claims = Jwts.claims().setSubject(authentication.getName())
        val now = Date()
        val expiresIn = Date(now.getTime() + expire_time)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiresIn)
            .signWith(SignatureAlgorithm.HS256, secret_key)
            .compact()
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     * @param token
     * @return
     */
    fun getAuthentication(token: String?): Authentication {
        val username = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).body.subject
        val userDetails = userDetailsService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     * @param req
     * @return
     */
    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    /**
     * 토큰을 검증
     * @param token
     * @return
     */
    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            // MalformedJwtException | ExpiredJwtException | IllegalArgumentException
            throw CustomException("Error on Token", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}