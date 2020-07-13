package com.example.springbootapiv2.events

import com.example.springbootapiv2.common.ErrorsResource
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI


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
    fun createEvent(@RequestBody eventDto: EventDto, errors: Errors): ResponseEntity<Any> {

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
    fun queryEvents(pageable: Pageable, assembler: PagedResourcesAssembler<Event>): ResponseEntity<Any> {

        val page = eventRepository.findAll(pageable)
        val pageResource = assembler.toModel(page){e -> EventResourcePaged(e)}
        pageResource.add(Link.of("http://localhost:8080/docs/index.html#resources-events-list").withRel("profile"))

        return ResponseEntity.ok(pageResource)
    }

    private fun badRequest(errors: Errors): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ErrorsResource(errors))
    }

}