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
        logger.info("Account balance: $balance")

        // Get transactions
        val sourceTransactions = transactionService.findBySourceAccount(accountNumber)
        val destinationTransactions = transactionService.findByDestinationAccount(accountNumber)
        val allTransactions = (sourceTransactions + destinationTransactions).distinctBy { it.id }

        logger.info("Total unique transactions fetched: ${allTransactions.size}")

        // Log all transactions for the account
        allTransactions.forEach { tr ->
            logger.info(
                "Transaction: id=${tr.id}, amount=${tr.amount}, source=${tr.sourceAccount}, destination=${tr.destinationAccount}, date=${tr.date}, flow=${tr.flow}"
            )
        }

        // Calculate total incoming and outgoing
        val totalIncoming = allTransactions.filter { it.destinationAccount == accountNumber && it.flow == "Incoming" }
            .sumOf { it.amount }
        val totalOutgoing =
            allTransactions.filter { it.sourceAccount == accountNumber && it.flow == "Outgoing" }.sumOf { it.amount }

        // Log calculated totals
        logger.info("Total incoming for account $accountNumber: $totalIncoming")
        logger.info("Total outgoing for account $accountNumber: $totalOutgoing")

        // Create overview DTO
        return AccountTransactionOverviewDTO(
            accountNumber = accountNumber,
            balance = balance,
            totalIncoming = totalIncoming,
            totalOutgoing = totalOutgoing
        )
    }

}
