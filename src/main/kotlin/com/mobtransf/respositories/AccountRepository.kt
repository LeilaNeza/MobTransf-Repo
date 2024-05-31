package com.mobtransf.respositories

import com.mobtransf.models.Account
import com.mobtransf.models.Client
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, String> {

    fun findByAccountHolder(accountHolder: Client): List<Account>
    fun findByAccountNumber(accountNumber: Long): Optional<Account>
    fun deleteByAccountHolder(accountHolder: Client)
}
