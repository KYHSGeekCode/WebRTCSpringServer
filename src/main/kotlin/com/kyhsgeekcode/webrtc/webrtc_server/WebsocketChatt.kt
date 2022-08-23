package com.kyhsgeekcode.webrtc.webrtc_server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.util.*
import javax.websocket.OnClose
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

@Service
@ServerEndpoint(value = "/chatt")
class WebsocketChatt {
    companion object {
        val clients = Collections.synchronizedSet(HashSet<Session>())
    }

    @OnMessage
    fun onMessage(msg: String, session: Session) {
        println("Got message: $msg")
        val websocketPacket = Json.decodeFromString<WebsocketPacket>(msg)
        when (websocketPacket.type) {
            "register_teacher" -> {
                val registerTeacher = Json.decodeFromString<RegisterTeacher>(websocketPacket.content)
            }
            "send_to" -> {
                val sendTo = Json.decodeFromString<SendTo>(websocketPacket.content)
                val target = sendTo.name
                val data = sendTo.data
            }
        }
    }

    @OnOpen
    fun onOpen(session: Session) {
        print("OnOpen $session")
        if (session !in clients) {
            clients.add(session)
        } else {
            print("Already open session")
        }
    }

    @OnClose
    fun onClose(session: Session) {

    }
}