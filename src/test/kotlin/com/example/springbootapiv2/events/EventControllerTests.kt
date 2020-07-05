package com.example.springbootapiv2.events

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest // 슬라이싱테스
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun createEvent() {
        mockMvc.perform(post("/api/events/ ")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated) // 201 응답이 create
    }

}