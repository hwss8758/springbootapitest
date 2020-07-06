package com.example.springbootapiv2.events

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@Controller
// 클래스 단위로 requestmapping 거는 경우 아래의 주석 제거하면 됨
//@RequestMapping(value = ["/api/events"], produces = [MediaTypes.HAL_JSON_VALUE])
class EventController {

    @PostMapping("/api/events")
    fun createEvent(@RequestBody event: Event): ResponseEntity<Event> {

        event.id = 10

        var createdUri: URI = WebMvcLinkBuilder.linkTo(EventController::class.java)
                .slash("api")
                .slash("events")
                .slash("{id}")
                .withSelfRel()
                .toUri()

        // URI return을 원하는 경우 아래 return값 사용
        //return ResponseEntity.created(createdUri).build()
        return ResponseEntity.created(createdUri).body(event)
    }
}