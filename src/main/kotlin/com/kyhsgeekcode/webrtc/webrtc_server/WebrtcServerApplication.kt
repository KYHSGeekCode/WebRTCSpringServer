package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.kyhsgeekcode"])
@EntityScan(basePackages = ["com.kyhsgeekcode"])
class WebrtcServerApplication

fun main(args: Array<String>) {
    runApplication<WebrtcServerApplication>(*args)
}
