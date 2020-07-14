package com.example.springbootapiv2.accounts

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Accounts, Int> {
    abstract fun findByEmail(username: String?): Accounts
}