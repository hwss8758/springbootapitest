package com.example.springbootapiv2.accounts

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun findByUsernameTest() {
        val inPassword: String = "wonsang"
        val inEmail: String = "hwss8758@aaa.com"

        // given
        val account: Accounts = Accounts(email = inEmail,
                password = inPassword,
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        accountService.saveAccount(account)

        // when
        val userDetailsService: UserDetailsService = accountService
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(inEmail)

        // then
        assertThat(passwordEncoder.matches(inPassword, userDetails.password)).isTrue()
    }

}