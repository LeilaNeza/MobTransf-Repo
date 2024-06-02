package com.mobtransf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [SecurityAutoConfiguration::class,
        UserDetailsServiceAutoConfiguration::class]
)
class MobtransfApplication

fun main(args: Array<String>) {
    runApplication<MobtransfApplication>(*args)
//	SpringApplication.run(MobtransfApplication::class.java, *args)

}
