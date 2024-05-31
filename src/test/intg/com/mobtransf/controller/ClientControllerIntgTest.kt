package com.mobtransf.controller

import com.mobtransf.dto.ClientDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ClientControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun addClient(){

        val clientDTO = ClientDTO("1200070024557162", "Ineza", "Leila", "0788919123","abc@gmail.com", "123")

        val savedClientDTO = webTestClient
            .post()
            .uri("/clients")
            .bodyValue(clientDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(ClientDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            savedClientDTO != null
        }
    }

}