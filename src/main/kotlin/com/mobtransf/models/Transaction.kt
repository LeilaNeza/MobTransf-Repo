package com.mobtransf.models

import com.mobtransf.dto.TransactionType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Transaction(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val amount: Double,
    val transactionType: TransactionType,
    val destinationAccount: Long,
    val sourceAccount: Long?,
    val date: LocalDateTime,
    val flow: String
)