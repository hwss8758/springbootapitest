package com.example.springbootapiv2.events

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfiguration {

    @Bean
    fun modelMapper(): ModelMapper = ModelMapper()
}