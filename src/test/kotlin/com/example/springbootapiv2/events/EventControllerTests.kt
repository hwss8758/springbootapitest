package com.example.springbootapiv2.events

import com.example.springbootapiv2.common.TestDescription
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc // springBootTest 어노테이션을 쓰면서 MockMvc를 사용하려고 하면 @AutoConfigureMockMvc를 사용하야한다.
class EventControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper // content를 JSON으로 변경하기 위해서 사용

    @Test
    @TestDescription("정상동작 테스트")
    fun createEvent() {

        var eventDto: EventDto = EventDto(name = "spring",
                description = "REST API Development with Spring",
                beginEnrollmentDateTime = LocalDateTime.of(2018, 11, 23, 14, 21),
                closeEnrollmentDateTime = LocalDateTime.of(2018, 11, 24, 14, 21),
                beginEventDateTime = LocalDateTime.of(2018, 11, 28, 14, 21),
                endEventDateTime = LocalDateTime.of(2018, 11, 29, 14, 21),
                basePrice = 100,
                maxPrice = 200,
                limitOfEnrollment = 100,
                location = "강남역 D2 스타텁 팩토리")

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated) // 201 응답이 create
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name))
    }

    @Test
    @TestDescription("event class 사용하여 입력할 경우 에러 확인")
    fun createEventBadRequest() {

        var event: Event = Event(name = "spring",
                description = "REST API Development with Spring",
                beginEnrollmentDateTime = LocalDateTime.of(2018, 11, 23, 14, 21),
                closeEnrollmentDateTime = LocalDateTime.of(2018, 11, 24, 14, 21),
                beginEventDateTime = LocalDateTime.of(2018, 11, 28, 14, 21),
                endEventDateTime = LocalDateTime.of(2018, 11, 26, 14, 21),
                basePrice = 100,
                maxPrice = 200,
                limitOfEnrollment = 100,
                location = "강남역 D2 스타텁 팩토리",
                id = 100,
                free = true,
                offline = false,
                eventStatus = EventStatus.PUBLISHED)

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @TestDescription("eventDto class 프로퍼티 검증 테스")
    fun createEvent_Bad_Request_Wrong_Input() {

        var eventDto: EventDto = EventDto(name = "spring",
                description = "REST API Development with Spring",
                beginEnrollmentDateTime = LocalDateTime.of(2018, 11, 26, 14, 21),
                closeEnrollmentDateTime = LocalDateTime.of(2018, 11, 25, 14, 21),
                beginEventDateTime = LocalDateTime.of(2018, 11, 24, 14, 21),
                endEventDateTime = LocalDateTime.of(2018, 11, 23, 14, 21),
                basePrice = 10000,
                maxPrice = 200,
                limitOfEnrollment = 100,
                location = "강남역 D2 스타텁 팩토리")

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())
    }


}