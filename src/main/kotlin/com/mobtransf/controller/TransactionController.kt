package com.mobtransf.controller

import com.mobtransf.dto.TransactionDTO
import com.mobtransf.services.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api")
class TransactionController(val transactionService: TransactionService) {

    @PostMapping("transfer")
    fun makeTransfer(
        @RequestBody transaction: TransactionDTO
    ): ResponseEntity<String> {
        // Delegate processing to TransactionService
        val result = transactionService.makeTransfer(transaction)

        // Translate the outcome of the transfer operation into an HTTP response
        return if (result.contains("Successful")) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.badRequest().body(result)
        }
    }

    @GetMapping("/accounts/{accountNumber}/transfers")
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllTransactions(@PathVariable accountNumber: Int): List<TransactionDTO> =
        transactionService.findByAccount(accountNumber)

}
