package com.example.springbootapiv2.configs

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId("event")
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.anonymous()
                ?.and()
                ?.authorizeRequests()
                ?.mvcMatchers(HttpMethod.GET, "/api/**")
                ?.permitAll()
                ?.anyRequest()
                ?.authenticated()
                ?.and()
                ?.exceptionHandling()
                ?.accessDeniedHandler(OAuth2AccessDeniedHandler())
    }
}