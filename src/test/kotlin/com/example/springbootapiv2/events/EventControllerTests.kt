package com.example.springbootapiv2.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime

@WebMvcTest // 슬라이싱테스
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper // content를 JSON으로 변경하기 위해서 사용

    @Test
    fun createEvent() {

        var event: Event = Event(name = "spring",
                description = "REST API Development with Spring",
                beginEnrollmentDateTime = LocalDateTime.of(2018, 11, 23, 14, 21),
                closeEnrollmentDateTime = LocalDateTime.of(2018, 11, 24, 14, 21),
                beginEventDateTime = LocalDateTime.of(2018, 11, 28, 14, 21),
                endEventDateTime = LocalDateTime.of(2018, 11, 26, 14, 21),
                basePrice = 100,
                maxPrice = 200,
                limitOfEnrollment = 100,
                location = "강남역 D2 스타텁 팩토리")

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated) // 201 응답이 create
                .andExpect(jsonPath("id").exists())
    }

}