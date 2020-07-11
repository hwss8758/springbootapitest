package com.example.springbootapiv2.events

import com.example.springbootapiv2.common.EventRestDocsMockMvcConfigurationCustomizer
import com.example.springbootapiv2.common.TestDescription
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc // springBootTest 어노테이션을 쓰면서 MockMvc를 사용하려고 하면 @AutoConfigureMockMvc를 사용하야한다.
@AutoConfigureRestDocs // springRestDocs를 사용하기 위한 어노테이션
@Import(EventRestDocsMockMvcConfigurationCustomizer::class) // restDocs를 이쁘게 보이도록 해당 클래스 임포트해준다.
@ActiveProfiles("test")
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
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                // restDocs 생성 => andDo(document())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of newevent"),
                                fieldWithPath("description").description("description of newevent"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of newevent"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of newevent"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of newevent"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of newevent"),
                                fieldWithPath("location").description("location of newevent"),
                                fieldWithPath("basePrice").description("basePrice of newevent"),
                                fieldWithPath("maxPrice").description("maxPrice of newevent"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of newevent")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("LOCATION header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        // 문서의 일부분만 생성할 경우 relaxedResponseFields 사용(relaxed prefix 추가): 아래의 주석 예제 참고
                        // 문서 전체를 생성 responseFields 사용
                        responseFields(
                                fieldWithPath("id").description("identifier of newevent"),
                                fieldWithPath("name").description("name of newevent"),
                                fieldWithPath("description").description("description of newevent"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of newevent"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of newevent"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of newevent"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of newevent"),
                                fieldWithPath("location").description("location of newevent"),
                                fieldWithPath("basePrice").description("basePrice of newevent"),
                                fieldWithPath("maxPrice").description("maxPrice of newevent"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of newevent"),
                                fieldWithPath("free").description("free or not"),
                                fieldWithPath("offline").description("offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update event list"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                        //                                relaxedResponseFields(
                        //                                fieldWithPath("id").description("identifier of newevent"),
                        //                        fieldWithPath("name").description("name of newevent"),
                        //                        fieldWithPath("description").description("description of newevent"),
                        //                        fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of newevent"),
                        //                        fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of newevent"),
                        //                        fieldWithPath("beginEventDateTime").description("beginEventDateTime of newevent"),
                        //                        fieldWithPath("endEventDateTime").description("endEventDateTime of newevent"),
                        //                        fieldWithPath("location").description("location of newevent"),
                        //                        fieldWithPath("basePrice").description("basePrice of newevent"),
                        //                        fieldWithPath("maxPrice").description("maxPrice of newevent"),
                        //                        fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of newevent"),
                        //                        fieldWithPath("free").description("free or not"),
                        //                        fieldWithPath("offline").description("offline or not"),
                        //                        fieldWithPath("eventStatus").description("event status"),
                        //                        fieldWithPath("_links.self.href").description("link to self"),
                        //                        fieldWithPath("_links.query-events.href").description("link to query event list"),
                        //                        fieldWithPath("_links.update-event.href").description("link to update event list")
                        //                )
                )
                )
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
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
    }


}