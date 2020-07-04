package com.example.springbootapiv2.events

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EventTest {

    @Test
    fun builder() {
        val event = Event()
        assertThat(event).isNotNull()
    }

    @Test
    fun javaBean() {
        val name: String = "Event"
        val description: String = "Spring"

        val event = Event()
        event.name = name
        event.description = description

        println(event)
        assertThat(event.name).isEqualTo(name)
        assertThat(event.description).isEqualTo(description)


    }
}