package com.mobtransf.dto

data class AccountDTO(
    val accountId: String?, // account_id
    val accountNumber: Long?,
    val accountType: AccountType,
    val accountHolder: ClientDTO?,
    var balance: Double = 0.0

)

//data class AccountRequest(val id: String)

//data class AccountResponse(val id: String, val type: AccountType)

//enum class AccountType { SAVINGS, CURRENT }