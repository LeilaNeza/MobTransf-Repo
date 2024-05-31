package com.mobtransf.models

import com.mobtransf.dto.ClientDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.io.Serializable

@Entity
//@Table(name = "Clients")
data class Client(
    @Id
    val nationalId: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var email: String,
    var passwordHash: String, // Store hashed password instead of plain text


) : Serializable {
    fun Client.toDTO(): ClientDTO {
        return ClientDTO(
            nationalId = this.nationalId,
            firstName = this.firstName,
            lastName = this.lastName,
            phoneNumber = this.phoneNumber,
            email = this.email,
            password = this.passwordHash
        )
    }

}