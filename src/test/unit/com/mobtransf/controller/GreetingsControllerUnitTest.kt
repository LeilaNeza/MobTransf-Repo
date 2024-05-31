package com.mobtransf.controller

import com.mobtransf.services.GreetingsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient
class GreetingsControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var greetingsServiceMock: GreetingsService //injecting instance of service

    @Test
    fun retrieveGreeting() {

        val name = "Ineza"

        every { greetingsServiceMock.retrieveGreetings(any()) } returns "$name, Hello from Default profile"

        val result = webTestClient.get()
            .uri("/home/greetings/{name}", name)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()
        Assertions.assertEquals("$name, Hello from Default profile", result.responseBody)


    }
}