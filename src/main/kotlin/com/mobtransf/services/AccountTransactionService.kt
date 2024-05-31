package com.mobtransf.services

import com.mobtransf.dto.AccountTransactionOverviewDTO
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AccountTransactionService(
    private val accountService: AccountService,
    private val transactionService: TransactionService
) {

    companion object : KLogging()

    fun getAccountTransactionOverview(accountNumber: Long): AccountTransactionOverviewDTO? {
//        val account = accountService.findByAccountNumber(accountNumber) ?: return null
        logger.info("In overview passed $accountNumber")
        val balance = accountService.viewBalance(accountNumber)!!
        val transactions = transactionService.findByAccount(accountNumber.toInt())
        for (tr in transactions) {
            logger.info("found id; ${tr.id} amount; ${tr.amount} source; ${tr.destinationAccount} destination; ${tr.sourceAccount} date; ${tr.date} flow: ${tr.flow}")
        }
        val totalIncoming = transactions.filter { it.destinationAccount == accountNumber }.sumOf { it.amount }
        val totalOutgoing = transactions.filter { it.sourceAccount == accountNumber }.sumOf { it.amount }

        return AccountTransactionOverviewDTO(
            accountNumber = accountNumber,
            balance = balance,
            totalIncoming = totalIncoming,
            totalOutgoing = totalOutgoing
        )
    }
}
