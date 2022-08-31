package com.kyhsgeekcode.webrtc.webrtc_server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.springframework.stereotype.Service
import java.util.*
import javax.websocket.OnClose
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

open class TeacherOrStudent(val name: String, val session: Session)

class Teacher(name: String, session: Session) : TeacherOrStudent(name, session)

class Student(name: String, session: Session) : TeacherOrStudent(name, session)

@Service
@ServerEndpoint(value = "/chatt")
class WebsocketChatt {
    companion object {
        private val clients = Collections.synchronizedSet(HashSet<TeacherOrStudent>())
        private val teachers = Collections.synchronizedSet(HashSet<Teacher>())
    }

    @OnMessage
    fun onMessage(msg: String, session: Session) {
        println("Got message: $msg")
        val websocketPacket = Json.decodeFromString<WebsocketPacket>(msg)
        val from = websocketPacket.from
        when (websocketPacket.type) {
            "register_teacher" -> {
                val registerTeacher = Json.decodeFromJsonElement<RegisterTeacher>(websocketPacket.content)
                if (!clients.any { it.name == from }) {
                    clients.add(Teacher(from, session))
                }
                session.basicRemote.sendText("OK")
            }
            "send_to" -> {
                val sendTo = Json.decodeFromJsonElement<SendTo>(websocketPacket.content)
                val target = sendTo.to
                val data = sendTo.data

                val targetClient = clients.find {
                    it.name == target
                }
                if (targetClient == null) {
                    session.basicRemote.sendText("Not found $target")
                    return
                }
                targetClient.session.basicRemote.sendText(msg)
            }
        }
    }

    @OnOpen
    fun onOpen(session: Session) {
        print("OnOpen $session")
    }

    @OnClose
    fun onClose(session: Session) {
        clients.removeIf {
            print("Removing $session")
            it.session == session
        }
    }
}