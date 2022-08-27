package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val userVo = userRepository.findByName(name = username ?: "").first()
        return CustomUserDetails(userVo)
    }
}