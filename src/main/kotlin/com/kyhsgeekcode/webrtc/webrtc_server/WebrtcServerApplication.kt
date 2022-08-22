package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class WebrtcServerApplication

fun main(args: Array<String>) {
    runApplication<WebrtcServerApplication>(*args)
}
