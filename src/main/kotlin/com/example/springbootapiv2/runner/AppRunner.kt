package com.example.springbootapiv2.runner

import com.example.springbootapiv2.accounts.AccountRole
import com.example.springbootapiv2.accounts.AccountService
import com.example.springbootapiv2.accounts.Accounts
import com.example.springbootapiv2.common.AppProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppRunner : ApplicationRunner {
    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var appProperties: AppProperties

    override fun run(args: ApplicationArguments?) {
        val accountAdmin: Accounts = Accounts(email = appProperties.adminUsername,
                password = appProperties.adminPassword,
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        val accountUser: Accounts = Accounts(email = appProperties.userUsername,
                password = appProperties.userPassword,
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        accountService.saveAccount(accountAdmin)
        accountService.saveAccount(accountUser)
    }
}