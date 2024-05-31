//package com.mobtransf.controller
//
//import com.mobtransf.services.GreetingsService
//import org.springframework.boot.logging.logback.LogbackLoggingSystem
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/home/greetings")
//class GreetingController(val greetingsService: GreetingsService) {
//
////    companion object : Klogging()
//
//    @GetMapping("/{name}")
//    fun retrieveGreeting(@PathVariable("name") name: String)
//        = greetingsService.retrieveGreetings(name)
//
//
//}