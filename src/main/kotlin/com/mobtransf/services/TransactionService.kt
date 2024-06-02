package com.mobtransf.services

import com.mobtransf.dto.TransactionDTO
import com.mobtransf.dto.TransactionType
import com.mobtransf.models.Transaction
import com.mobtransf.respositories.TransactionRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(val accountService: AccountService, val transactionRepository: TransactionRepository) {

    companion object : KLogging()

    fun makeTransfer(transactionDTO: TransactionDTO): String {
        logger.info(
            "source account :${transactionDTO.sourceAccount} " +
                    "destination account :${transactionDTO.destinationAccount}" +
                    "amount :${transactionDTO.amount}"
        )
        val sourceAccount =
            accountService.findByAccountNumber(transactionDTO.sourceAccount!!) ?: return "Account missing"
        val destinationAccount =
            accountService.findByAccountNumber(transactionDTO.destinationAccount) ?: return "Account missing"

        logger.info("source account: ${sourceAccount.accountNumber}, destination account: ${destinationAccount.accountNumber}")
        sourceAccount.let {
            if (it.accountNumber != destinationAccount.accountNumber) {
                // Ensure sender has enough balance for the transfer

                if (it.balance < transactionDTO.amount) {
                    return "Transfer failed! Insufficient balance"
//                    throw InsufficientBalanceException("Insufficient balance for transfer")
                } else {
                    // Update sender's balance
                    it.balance -= transactionDTO.amount
                    /*val updatedAccountDTO = AccountDTO(
                        it.id,
                        it.accountNo,
                        it.balance
                    )*/
                    accountService.updateAccount(it)

                    // Record transaction
                    val sourceTransaction = Transaction(
                        id = null,
                        sourceAccount = it.accountNumber,
                        destinationAccount = destinationAccount.accountNumber!!,
                        amount = transactionDTO.amount,
                        transactionType = TransactionType.TRANSFER,
                        date = LocalDateTime.now(),
                        flow = "Outgoing"
                    )
                    transactionRepository.save(sourceTransaction)
                    logger.info("Transaction occurred :${sourceTransaction.transactionType}")
                }
            } else {
                return "Transfer failed! Destination must differ from source"
            }

        }

        destinationAccount.let {
            // Update receiver's balance
            it.balance += transactionDTO.amount
            accountService.updateAccount(it)

            // Record transactions

            val destinationTransaction = Transaction(
                id = null,
                destinationAccount = it.accountNumber!!,
                sourceAccount = sourceAccount.accountNumber,
                amount = transactionDTO.amount,
                transactionType = TransactionType.TRANSFER,
                date = LocalDateTime.now(),
                flow = "Incoming"
            )
            transactionRepository.save(destinationTransaction)
            logger.info("Transaction occurred :${destinationTransaction.transactionType}")

            return "Transfer Successful!"
        }
    }

    fun findByAccount(accountNumber: Int): List<TransactionDTO> {
        logger.info("passed account $accountNumber")
        val sourceTransactions = transactionRepository.findBySourceAccount(accountNumber.toLong())
        val destinationTransactions = transactionRepository.findByDestinationAccount(accountNumber.toLong())
        for (tr in sourceTransactions) {
            logger.info("found id; ${tr.id} amount; ${tr.amount} source; ${tr.destinationAccount} destination; ${tr.sourceAccount} date; ${tr.date} flow: ${tr.flow}")
        }
        for (tr in destinationTransactions) {
            logger.info("found id; ${tr.id} amount; ${tr.amount} source; ${tr.destinationAccount} destination; ${tr.sourceAccount} date; ${tr.date} flow: ${tr.flow}")
        }

        val transactions = sourceTransactions + destinationTransactions
        for (tr in transactions) {
            logger.info("found id; ${tr.id} amount; ${tr.amount} source; ${tr.destinationAccount} destination; ${tr.sourceAccount} date; ${tr.date} flow: ${tr.flow}")
        }
        return transactions.map { transaction ->
            logger.info("id; ${transaction.id} amount; ${transaction.amount} source; ${transaction.destinationAccount} destination; ${transaction.sourceAccount} date; ${transaction.date} flow: ${transaction.flow} ")
            TransactionDTO(
                id = transaction.id,
                amount = transaction.amount,
                destinationAccount = transaction.destinationAccount,
                sourceAccount = transaction.sourceAccount,
                date = transaction.date,
                transactionType = transaction.transactionType,
                flow = if (transaction.sourceAccount == accountNumber.toLong()) "Outgoing" else "Incoming"
            )
        }
    }

    fun findBySourceAccount(accountNumber: Long): List<TransactionDTO> {
        return transactionRepository.findBySourceAccount(accountNumber)
    }

    fun findByDestinationAccount(accountNumber: Long): List<TransactionDTO> {
        return transactionRepository.findByDestinationAccount(accountNumber)
    }


}