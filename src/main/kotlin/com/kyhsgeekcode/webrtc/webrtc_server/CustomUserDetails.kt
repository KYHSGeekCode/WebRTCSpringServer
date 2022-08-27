package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    userVo: UserVo
) : UserDetails {
    private val id = userVo.id
    private val name = userVo.name
    private val password = userVo.password
    private val authorities //권한 목록
        : Collection<GrantedAuthority> = listOf()

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}