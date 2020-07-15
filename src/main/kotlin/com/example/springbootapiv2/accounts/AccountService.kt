package com.example.springbootapiv2.accounts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.stream.Collectors


@Service
class AccountService : UserDetailsService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun saveAccount(account: Accounts): Accounts {
        account.password = passwordEncoder.encode(account.password)
        return accountRepository.save(account)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        // if (null != obj) ...을 대체하기 위해서 safe Call(?)과 함께 let() 함수를 사용
        // val accounts: Accounts = accountRepository.findByEmail(username)

        val accounts: Accounts? = username?.let { accountRepository.findByEmail(it) }
        // AccountAdapter가 만들어져서 아래 코드 주석 코드 새로 작성
        //return User(accounts?.email, accounts?.password, accounts?.roles?.let { authorities(it) })

        return AccountAdapter(accounts!!)
    }

    private fun authorities(roles: Set<AccountRole>): MutableCollection<out GrantedAuthority>? {
        return roles.stream()
                .map { r: AccountRole -> SimpleGrantedAuthority("ROLE_" + r.name) }
                .collect(Collectors.toSet())
    }
}