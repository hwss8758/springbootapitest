package com.example.springbootapiv2.common

import org.springframework.stereotype.Component

@Component
data class AppProperties(val adminUsername: String = "admin@aaa.com",
                         val adminPassword: String = "admin",
                         val userUsername: String = "user@aaa.com",
                         val userPassword: String = "user",
                         val clientId: String = "myApp",
                         val clientSecret: String = "pass")