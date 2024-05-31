package com.mobtransf.dto

data class AccountTransactionOverviewDTO(
    val accountNumber: Long,
    val balance: Double,
    val totalIncoming: Double,
    val totalOutgoing: Double
)
