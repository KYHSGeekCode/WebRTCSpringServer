package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.http.HttpStatus

class CustomException(
    message: String,
    val status: HttpStatus
) : Exception(message)