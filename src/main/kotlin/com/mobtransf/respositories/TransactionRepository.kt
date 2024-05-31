package com.mobtransf.respositories

import com.mobtransf.dto.TransactionDTO
import com.mobtransf.models.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByDestinationAccount(accountNumber: Long?): List<TransactionDTO>
    fun findBySourceAccount(accountNumber: Long?): List<TransactionDTO>
}