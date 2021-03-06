package com.example.springbootapiv2.events

import com.example.springbootapiv2.accounts.AccountRepository
import com.example.springbootapiv2.accounts.AccountRole
import com.example.springbootapiv2.accounts.AccountService
import com.example.springbootapiv2.accounts.Accounts
import com.example.springbootapiv2.common.AppProperties
import com.example.springbootapiv2.common.BaseControllerTest
import com.example.springbootapiv2.common.TestDescription
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.event.annotation.BeforeTestMethod
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.stream.IntStream

class EventControllerTests : BaseControllerTest() {

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var appProperties: AppProperties

    @BeforeEach
    fun setup() {
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

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
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
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
                                fieldWithPath("manager.id").description("manager id"),
//                                fieldWithPath("manager.email").description("manager email"),
//                                fieldWithPath("manager.password").description("manager password"),
//                                fieldWithPath("manager.roles").description("manager roles"),
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

    private fun getAccessToken(): String {

        val username: String = appProperties.userUsername
        val password: String = appProperties.userPassword

        // given
        val account: Accounts = Accounts(email = username,
                password = password,
                roles = setOf(AccountRole.ADMIN, AccountRole.USER))

        accountService.saveAccount(account)

        val clientId: String = appProperties.clientId
        val clientSecret: String = appProperties.clientSecret

        val perform = mockMvc.perform(post("/oauth/token")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
        println("perform:"+perform)
        val responseBody = perform.andReturn().response.getContentAsString()
        println("responseBody:"+responseBody)
        val parser = Jackson2JsonParser()
        println("parser:"+parser)

        return parser.parseMap(responseBody).get("access_token").toString()
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
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

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    fun queryEventTest() {

        IntStream.range(0, 30).forEach {
            generateEvent(it)
        }

        mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResourcePagedList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    fun queryEventAuthenticationTest() {

        IntStream.range(0, 30).forEach {
            generateEvent(it)
        }

        mockMvc.perform(get("/api/events")
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResourcePagedList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))
    }

    @Test
    @TestDescription("기존의 Event 하나 조회하기")
    fun getEventTest() {

        //Given
        val event = generateEvent(100)

        //when&then
        mockMvc.perform(get("/api/events/{id}", event.id))
                .andExpect(status().isOk)
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
    }

    @Test
    @TestDescription("없는 Event 하나 조회하여 404 응답받기")
    fun getEventTest404() {

        //when&then
        mockMvc.perform(get("/api/events/1188"))
                .andExpect(status().isNotFound)
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    fun updateEventTest() {

        //given
        val eventName = "Updated Event"
        val event: Event = generateEvent(200)
        val eventDto: EventDto = modelMapper.map(event, EventDto::class.java)
        eventDto.name = eventName

        println("*** check event : " + event)
        println("*** check eventDto : " + eventDto)

        //when & then
        mockMvc.perform(put("/api/events/{id}", event.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 이벤트 수정 실패")
    fun updateEventErrorTest() {

        //given
        val eventName = "Updated Event"
        val event: Event = generateEvent(200)
        val eventDto: EventDto = modelMapper.map(event, EventDto::class.java)
        eventDto.name = eventName
        eventDto.basePrice = 20000
        eventDto.maxPrice = 1000

        //when & then
        mockMvc.perform(put("/api/events/{id}", event.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    fun updateEventErrorTest404() {

        //given
        val eventName = "Updated Event"
        val event: Event = generateEvent(200)
        val eventDto: EventDto = modelMapper.map(event, EventDto::class.java)
        eventDto.name = eventName

        //when & then
        mockMvc.perform(put("/api/events/1123122")
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound)
    }

    private fun generateEvent(index: Int): Event {
        val event: Event = Event(name = "spring$index",
                description = "REST API Development with Spring",
                beginEnrollmentDateTime = LocalDateTime.of(2018, 11, 23, 14, 21),
                closeEnrollmentDateTime = LocalDateTime.of(2018, 11, 24, 14, 21),
                beginEventDateTime = LocalDateTime.of(2018, 11, 28, 14, 21),
                endEventDateTime = LocalDateTime.of(2018, 11, 29, 14, 21),
                basePrice = 100,
                maxPrice = 200,
                limitOfEnrollment = 100,
                location = "강남역 D2 스타텁 팩토리",
                id = 100,
                free = true,
                offline = false,
                eventStatus = EventStatus.PUBLISHED)
        return eventRepository.save(event)
    }


}