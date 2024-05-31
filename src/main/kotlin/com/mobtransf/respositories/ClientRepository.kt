package com.mobtransf.respositories

import com.mobtransf.models.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, String> {
    fun findByNationalId(nationalId: String): Client?
    fun findByEmail(email: String): Client?
}