package com.kyhsgeekcode.webrtc.webrtc_server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
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

        const val TYPE = "type"
        const val OFFER = "offer"
        const val ANSWER = "answer"
        const val CANDIDATE = "candidate"
        const val SDP = "sdp"

        const val EMIT_JOIN = "join"
        const val EMIT_MESSAGE = "message"
        const val EMIT_HANGUP = "hangup"

        const val ON_MATCHED = "matched"
        const val ON_WAITING_STATUS = "waiting_status"
        const val ON_TERMINATED = "terminated"

    }

    @OnMessage
    fun onMessage(msg: String, session: Session) {
        println("Got message: $msg")
        val websocketPacket = Json.decodeFromString<WebsocketPacket>(msg)
        val from = websocketPacket.from
        when (websocketPacket.event) {
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
            EMIT_MESSAGE -> {
                println("EMIT_MESSAGE: $msg")
                if (clients.size != 2) {
                    println("Not exact number of clients: ${clients.size}")
                    return
                }
                val targetClient = clients.find {
                    it.name != from
                }
                if (targetClient == null) {
                    println("Not found diff from $from")
                    return
                }
                println("Sending to ${targetClient.name}")
                targetClient.session.basicRemote.sendText(msg)
            }
            EMIT_JOIN -> {
                print("EMIT_JOIN")
                val emitJoinData = Json.decodeFromJsonElement<EmitJoinData>(websocketPacket.content)
                if (!clients.any { it.name == from }) {
                    if (emitJoinData.token == "teacher") {
                        val teacher = Teacher(from, session)
                        clients.add(teacher)
                        teachers.add(teacher)
                    } else {
                        clients.add(Student(from, session))
                    }
                    println("Added $from to clients as ${emitJoinData.token}")
                } else {
                    println("$from already exists in clients")
                }
                if (teachers.size == 1 && clients.size == 2) { // student and teacher both exists
                    // send matched
                    val teacher = teachers.first()
                    val student = clients.find { it is Student } as Student
                    teacher.session.basicRemote.sendText(
                        Json.encodeToString(
                            WebsocketPacket(
                                from = "server",
                                event = ON_MATCHED,
                                content = Json.encodeToJsonElement(
                                    MatchModel(
                                        offer = true,
                                        otherIdx = 1,
                                        duration = 10,
                                        matchIdx = 0,
                                        previewText = "preview text",
                                        previewTime = 10,
                                        skin = emitJoinData.skin
                                    )
                                )
                            )
                        )
                    )
                    student.session.basicRemote.sendText(
                        Json.encodeToString(
                            WebsocketPacket(
                                from = "server",
                                event = ON_MATCHED,
                                content = Json.encodeToJsonElement(
                                    MatchModel(
                                        offer = false,
                                        otherIdx = 0,
                                        duration = 10,
                                        matchIdx = 0,
                                        previewText = "preview text",
                                        previewTime = 10,
                                        skin = emitJoinData.skin
                                    )
                                )
                            )
                        )
                    )
                }
//                session.basicRemote.sendText("OK")
            }
            EMIT_HANGUP -> {
                println("EMIT_HANGUP")
            }
            CANDIDATE -> {
                // id, label, candidate
                println("CANDIDATE")
            }
        }
    }

    @OnOpen
    fun onOpen(session: Session) {
        println("OnOpen ${session.id}")
    }

    @OnClose
    fun onClose(session: Session) {
        clients.removeIf {
            println("Removing $session")
            it.session == session
        }
    }
}