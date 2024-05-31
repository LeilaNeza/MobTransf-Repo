package com.mobtransf.controller

import com.mobtransf.dto.AccountTransactionOverviewDTO
import com.mobtransf.dto.ClientDTO
import com.mobtransf.dto.LoginRequest
import com.mobtransf.services.AccountService
import com.mobtransf.services.AccountTransactionService
import com.mobtransf.services.ClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/clients")
class ClientController(
    val clientService: ClientService,
    val accountService: AccountService,
    val accountTransactionService: AccountTransactionService,
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val username = loginRequest.email
        val password = loginRequest.password

        val clientDTO = clientService.login(username, password)
            ?: return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid username or password")

        return ResponseEntity(clientDTO, HttpStatus.OK)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllClients(): List<ClientDTO> = clientService.findAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addClient(@RequestBody client: ClientDTO): ResponseEntity<ClientDTO> {
        val createdClient = clientService.addClient(client)
        return if (createdClient != null) {
            ResponseEntity(createdClient, HttpStatus.CREATED)
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PutMapping("/{nid}")
    fun updateClient(@PathVariable nid: String, @RequestBody client: ClientDTO) =
        clientService.updateClient(nid, client)

    @DeleteMapping("/{nid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeClient(@PathVariable nid: String) = clientService.deleteClient(nid)

    @GetMapping("/{nid}")
    @ResponseStatus(HttpStatus.OK)
    fun findByNationalId(@PathVariable nid: String): ResponseEntity<ClientDTO> {
        val clientDTO = clientService.findByNationalId(nid)
        return if (clientDTO != null) {
            ResponseEntity(clientDTO, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
//        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/{clientId}/accounts/overview")
    @ResponseStatus(HttpStatus.OK)
    fun getClientAccountOverviews(@PathVariable clientId: String): List<AccountTransactionOverviewDTO> {
        val client = clientService.findByNationalId(clientId) ?: return emptyList()

        val accounts = accountService.findBYAccountHolder(client.nationalId) ?: return emptyList()

        return accounts.map { account ->
            accountTransactionService.getAccountTransactionOverview(account.accountNumber!!)!!
        }
    }
}
