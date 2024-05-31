package com.mobtransf.models

import com.mobtransf.dto.AccountType
import jakarta.persistence.*
import java.io.Serializable

@Entity
//@Table(name = "ACCOUNTS")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    @Column(unique = true)
    val accountNumber: Long?,
    val accountType: AccountType,
    @ManyToOne(fetch = FetchType.LAZY)
    val accountHolder: Client,
    var balance: Double = 0.0,

    ) : Serializable