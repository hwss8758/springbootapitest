package com.example.springbootapiv2.accounts

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.stream.Collectors

class AccountAdapter(_account: Accounts) : User(_account.email, _account.password, authorities(_account.roles!!)) {

    private val account: Accounts = _account

    fun getAccount(): Accounts {
        return account
    }

    companion object {
        private fun authorities(roles: Set<AccountRole>): Collection<GrantedAuthority?> {
            return roles.stream()
                    .map { r: AccountRole -> SimpleGrantedAuthority("ROLE_" + r.name) }
                    .collect(Collectors.toSet())
        }
    }
}