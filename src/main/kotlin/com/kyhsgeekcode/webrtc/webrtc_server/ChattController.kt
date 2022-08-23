package com.kyhsgeekcode.webrtc.webrtc_server

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class ChattController {
    @RequestMapping("/mychatt")
    fun chatt(): ModelAndView {
        return ModelAndView().apply {
            viewName = "chatting"
        }
    }
}