package com.mobtransf.controller

import com.mobtransf.dto.AccountDTO
import com.mobtransf.services.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api")
class AccountController(val accountService: AccountService) {

    @GetMapping("/clients/{nid}/accounts")
    @ResponseStatus(HttpStatus.OK)
    fun findAll(@PathVariable nid: String): ResponseEntity<List<AccountDTO>> {
        val accounts = accountService.findBYAccountHolder(nid)!!
        return ResponseEntity.ok(accounts)
    }

    @PostMapping("/clients/{nid}/accounts") // POST /clients/{nid}/accounts
    @ResponseStatus(HttpStatus.CREATED)
    fun addAccount(@PathVariable nid: String, @RequestBody account: AccountDTO): ResponseEntity<AccountDTO> {

        val createdAccount = accountService.addAccount(nid, account)
        // Translate the outcome of the transfer operation into an HTTP response
        return if (createdAccount == null) {
            ResponseEntity.badRequest().body(createdAccount)

        } else {
            ResponseEntity(createdAccount, HttpStatus.CREATED)
        }
    }

    @DeleteMapping("/accounts/{id}")
    fun removeAccount(@PathVariable id: String): ResponseEntity<Void> {
        accountService.deleteAccount(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/accounts/{accountNumber}")
    fun findByAccountNumber(@PathVariable accountNumber: Int): ResponseEntity<AccountDTO> {
        val account = accountService.findByAccountNumber(accountNumber.toLong())
        return ResponseEntity.ok(account)
    }

    @GetMapping("/accounts/{accountNumber}/balance")
    fun getBalance(@PathVariable accountNumber: Int): ResponseEntity<Double> {
        val balance = accountService.viewBalance(accountNumber.toLong())!!
        return ResponseEntity.ok(balance)
    }

}