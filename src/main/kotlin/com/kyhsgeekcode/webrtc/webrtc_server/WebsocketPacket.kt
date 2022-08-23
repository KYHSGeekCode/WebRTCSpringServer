package com.kyhsgeekcode.webrtc.webrtc_server

import kotlinx.serialization.Serializable


@Serializable
data class WebsocketPacket(
    val type: String,
    val content: String
)

@Serializable
data class RegisterTeacher(
    val name: String
)

@Serializable
data class SendTo(
    val name: String,
    val data: String
)