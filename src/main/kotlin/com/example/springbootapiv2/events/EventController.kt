package com.example.springbootapiv2.events

import com.example.springbootapiv2.accounts.AccountAdapter
import com.example.springbootapiv2.accounts.Accounts
import com.example.springbootapiv2.accounts.CurrentUser
import com.example.springbootapiv2.common.ErrorsResource
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*


@Controller
class EventController {

    @Autowired
    private lateinit var eventRepository: EventRepository

    // EventConfiguration 클래스에서 bean으로 생성했음으로 의존성 주입이 가능함.
    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var eventValidator: EventValidator

    @PostMapping("/api/events")
    fun createEvent(@RequestBody eventDto: EventDto,
                    errors: Errors,
                    @CurrentUser currentUser: Accounts): ResponseEntity<Any> {

        if (errors.hasErrors()) {
            return badRequest(errors)
        }

        eventValidator.validate(eventDto, errors)
        if (errors.hasErrors()) {
            return badRequest(errors)
        }
        // ModelMapper 사용하여 클래스 매핑
        val event: Event = modelMapper.map(eventDto, Event::class.java)

        // free 항목 설정
        event.update()

        // manager 항목 설정
        event.manager = currentUser

        val eventId = eventRepository.save(event).id!!

        // eventRepository.findById(eventId)의 리턴값이 Optional임으로 클래스 객체를 받으려면 get()함수가 필요
        val newEvent: Event = eventRepository.findById(eventId).get()

        // baseLink 생성
        val baseLink: WebMvcLinkBuilder = WebMvcLinkBuilder.linkTo(EventController::class.java)
                .slash("api")
                .slash("events")
                .slash(newEvent.id)

        val createdUri: URI = baseLink.toUri()

        // spring hateoas 적용하기 위해서 href 내역 추가
        val eventResource: EventResource = EventResource(newEvent)

        eventResource.add(baseLink.withRel("query-events"))
        //eventResource.add(baseLink.withSelfRel())
        eventResource.add(baseLink.withRel("update-event"))
        eventResource.add(Link.of("http://localhost:8080/docs/index.html").withRel("profile"))

        return ResponseEntity.created(createdUri).body(eventResource)
    }

    @GetMapping("/api/events")
    fun queryEvents(pageable: Pageable,
                    assembler: PagedResourcesAssembler<Event>,
                    @CurrentUser account: Accounts?): ResponseEntity<Any> {
        // 아래의 숫자 순서대로 코드가 변경됨
        //3. @AuthenticationPrincipal(expression = "account") account: Accounts?): ResponseEntity<Any> {
        //2. @AuthenticationPrincipal currentUser: AccountAdapter?): ResponseEntity<Any> {
        //1. @AuthenticationPrincipal user: User?): ResponseEntity<Any> {
        val page = eventRepository.findAll(pageable)
        val pageResource = assembler.toModel(page) { e -> EventResourcePaged(e) }
        pageResource.add(Link.of("http://localhost:8080/docs/index.html#resources-events-list").withRel("profile"))

//if (user != null) {
//if (currentUser != null) {
        if (account != null) {
            pageResource.add(WebMvcLinkBuilder.linkTo(EventController::class.java)
                    .slash("api")
                    .slash("events")
                    .withRel("create-event"))
        }

        return ResponseEntity.ok(pageResource)
    }

    @GetMapping("/api/events/{id}")
    fun getEvent(@PathVariable id: Int,
                 @CurrentUser currentUser: Accounts?): ResponseEntity<Any> {
        val optionalEvent: Optional<Event> = eventRepository.findById(id)

        if (!optionalEvent.isPresent) {
            return ResponseEntity.notFound().build()
        }

        val event: Event = optionalEvent.get()
        val eventResource = EventResource(event)

        eventResource.add(Link.of("http://localhost:8080/docs/index.html#resources-events-get").withRel("profile"))

        if (event.manager == currentUser) {
            eventResource.add(WebMvcLinkBuilder.linkTo(EventController::class.java)
                    .slash("api")
                    .slash("events")
                    .slash(event.id)
                    .withRel("create-event"))
        }

        return ResponseEntity.ok(eventResource)
    }

    @PutMapping("/api/events/{id}")
    fun updateEvent(@PathVariable id: Int,
                    @RequestBody @Validated eventDto: EventDto,
                    errors: Errors,
                    @CurrentUser currentUser: Accounts?): ResponseEntity<Any> {
        val optionalEvent: Optional<Event> = eventRepository.findById(id)

        if (!optionalEvent.isPresent) {
            return ResponseEntity.notFound().build()
        }

        if (errors.hasErrors()) {
            return badRequest(errors)
        }

        eventValidator.validate(eventDto, errors)
        if (errors.hasErrors()) {
            return badRequest(errors)
        }

        val existingEvent: Event = optionalEvent.get()
        if (existingEvent.manager != null &&
                existingEvent.manager != currentUser) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        modelMapper.map(eventDto, existingEvent)
        val savedEvent = eventRepository.save(existingEvent)

        val eventResource = EventResource(savedEvent)
        eventResource.add(Link.of("http://localhost:8080/docs/index.html#resources-events-update").withRel("profile"))

        return ResponseEntity.ok(eventResource)
    }

    private fun badRequest(errors: Errors): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ErrorsResource(errors))
    }

}