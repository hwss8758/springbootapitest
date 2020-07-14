package com.example.springbootapiv2.runner

import com.example.springbootapiv2.accounts.AccountRole
import com.example.springbootapiv2.accounts.AccountService
import com.example.springbootapiv2.accounts.Accounts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppRunner : ApplicationRunner {
    @Autowired
    lateinit var accountService: AccountService

    override fun run(args: ApplicationArguments?) {
        val account: Accounts = Accounts(email = "hwss8758@aaa.com",
                password = "wonsang",
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        accountService.saveAccount(account)
    }
}