package com.example.springbootapiv2.configs

import com.example.springbootapiv2.accounts.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

// WebSecurityConfigurerAdapter()를 상속받으면 모든 요청에 인증을 받도록 할수 있다. (spring security의 기술)
@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Bean
    fun tokenStore(): TokenStore {
        return InMemoryTokenStore()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(accountService)
                ?.passwordEncoder(passwordEncoder)
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.mvcMatchers("/docs/index.html")
        web?.ignoring()?.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

//    override fun configure(http: HttpSecurity?) {
//        http?.authorizeRequests()
//                ?.mvcMatchers("/docs/index.html")?.anonymous()
//                ?.requestMatchers(PathRequest.toStaticResources().atCommonLocations())?.anonymous()
//    }

//    override fun configure(http: HttpSecurity?) {
//        http?.anonymous()
//                ?.and()
//                ?.formLogin()
//                ?.and()
//                ?.authorizeRequests()
//                ?.mvcMatchers(HttpMethod.GET, "/api/**")?.authenticated()
//                ?.anyRequest()
//                ?.authenticated()
//    }
}