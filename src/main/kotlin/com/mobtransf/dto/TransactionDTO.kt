package com.mobtransf.dto

import java.time.LocalDateTime

data class TransactionDTO(
    val id: Long?,
    val amount: Double,
    val destinationAccount: Long,
    val sourceAccount: Long?,
    val date: LocalDateTime?,
    val flow: String?,
    val transactionType: TransactionType?
)