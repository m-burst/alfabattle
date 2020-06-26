package com.github.m_burst.alfabattle.task5.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping("/path")
    fun get(): String {
        return "Hello World"
    }
}
