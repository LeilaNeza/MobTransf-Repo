package com.mobtransf.services

import com.mobtransf.models.Client
import com.mobtransf.respositories.AccountRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AccountDeletionService(
    private val accountRepository: AccountRepository
) {

    companion object : KLogging()

    fun deleteAccountsByAccountHolder(client: Client) {
        accountRepository.deleteByAccountHolder(client)
        logger.info("Deleted all accounts for client with ID: ${client.nationalId}")
    }
}
