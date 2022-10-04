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
    val data: JsonElement
)

@Serializable
data class EmitJoinData(val token: String, val password: String, val skin: String)


@Serializable
data class MatchModel(
    val offer: Boolean,
    val otherIdx: Int,
    val duration: Int,
    val matchIdx: Int,
    val previewText: String?,
    val previewTime: Int?,
    val skin: String
)