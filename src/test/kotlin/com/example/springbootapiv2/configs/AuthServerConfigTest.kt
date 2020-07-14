package com.example.springbootapiv2.configs

import com.example.springbootapiv2.accounts.AccountRepository
import com.example.springbootapiv2.accounts.AccountRole
import com.example.springbootapiv2.accounts.AccountService
import com.example.springbootapiv2.accounts.Accounts
import com.example.springbootapiv2.common.BaseControllerTest
import com.example.springbootapiv2.common.TestDescription
import com.example.springbootapiv2.events.EventRepository
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

class AuthServerConfigTest : BaseControllerTest() {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var eventRepository: EventRepository

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    fun getAuthToken() {

        // test전 메모리 DB clear
        accountRepository.deleteAll()
        eventRepository.deleteAll()

        val username: String = "hwss8758@aaa.com"
        val password: String = "wonsang"

        // given
        val account: Accounts = Accounts(email = username,
                password = password,
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        accountService.saveAccount(account)

        val clientId: String = "myApp"
        val clientSecret: String = "pass"

        mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("access_token").exists())
    }

}