package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserVo, Long> {
    fun findByName(name: String): List<UserVo>
}