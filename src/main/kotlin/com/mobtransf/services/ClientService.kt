package com.mobtransf.services

import com.mobtransf.dto.ClientDTO
import com.mobtransf.models.Client
import com.mobtransf.respositories.ClientRepository
import mu.KLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ClientService(
    val clientRepository: ClientRepository,
    private val accountDeletionService: AccountDeletionService,
    private val passwordEncoder: PasswordEncoder // Inject the PasswordEncoder bean
) {

    companion object : KLogging()

    fun login(email: String, password: String): ClientDTO? {
        val existingClient = clientRepository.findByEmail(email)
        if (existingClient == null) {
            logger.info("found no such client with email : $email and password: $password")
            return null
        } else {
            logger.info("found client ${existingClient.firstName}")
            return if (!passwordEncoder.matches(password, existingClient.passwordHash)) {
                null
            } else {
                existingClient.toDTO()
            }
        }
    }

    fun findByNationalId(nid: String): ClientDTO? {
        val existingClient = clientRepository.findByNationalId(nid)
        logger.info("Client found :${existingClient?.firstName}")
        print("client: ${existingClient.toDTO()}")
        return existingClient.let {
            ClientDTO(
                nationalId = it!!.nationalId,
                firstName = it.firstName,
                lastName = it.lastName,
                phoneNumber = it.phoneNumber,
                email = it.email,
                password = "",
            )
        }
    }

    fun addClient(clientDTO: ClientDTO): ClientDTO? {
        val existingClient = clientRepository.findById(clientDTO.nationalId)
        if (existingClient.isPresent) {
            return null
        } else {
            // Hash the password
            val passwordHash = passwordEncoder.encode(clientDTO.password)

            val client = Client(
                nationalId = clientDTO.nationalId,
                firstName = clientDTO.firstName,
                lastName = clientDTO.lastName,
                phoneNumber = clientDTO.phoneNumber,
                email = clientDTO.email,
                passwordHash = passwordHash
            )
            val savedClient = clientRepository.save(client)
            logger.info("Client saved :${savedClient.firstName}")
            return savedClient.let {
                ClientDTO(
                    nationalId = it.nationalId,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    phoneNumber = it.phoneNumber,
                    email = it.email,
                    password = "",
                )
            }
        }
    }

    fun findAll(): List<ClientDTO> {
        return clientRepository.findAll().map {
            ClientDTO(it.nationalId, it.firstName, it.lastName, it.email, it.phoneNumber, "***")
        }
    }

    fun updateClient(nid: String, client: ClientDTO): Boolean {
        val existingClient = clientRepository.findById(nid)
        return if (existingClient.isPresent) {
            val updateClient = existingClient.get().apply {
                firstName = client.firstName
                lastName = client.lastName
                email = client.email
                phoneNumber = client.phoneNumber
                passwordHash = passwordEncoder.encode(client.password) // Update hashed password
            }
            clientRepository.save(updateClient)
            true
        } else {
            false
        }
    }

    fun deleteClient(nid: String) {
        val existingClient = clientRepository.findById(nid)
        if (existingClient.isPresent) {
            val client = existingClient.get()
            // Delete each account
            accountDeletionService.deleteAccountsByAccountHolder(client)
            // Delete the client
            clientRepository.deleteById(client.nationalId)
        }
    }
}

private fun Client?.toDTO(): ClientDTO? {
    return this?.let {
        ClientDTO(
            nationalId = it.nationalId,
            firstName = this.firstName,
            lastName = this.lastName,
            phoneNumber = this.phoneNumber,
            email = this.email,
            password = this.passwordHash
        )
    }
}
