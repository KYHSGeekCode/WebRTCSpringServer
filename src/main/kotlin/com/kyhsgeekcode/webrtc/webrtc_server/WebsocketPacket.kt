package com.kyhsgeekcode.webrtc.webrtc_server

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class WebsocketPacket(
    val from: String,
    val event: String,
    val content: JsonElement
)

@Serializable
data class RegisterTeacher(
    val classname: String
)

@Serializable
data class SendTo(
    val to: String,
    val data: String
)
