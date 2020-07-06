package com.example.springbootapiv2.events

import org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@Controller
class EventController {

    @PostMapping("/api/events")
    fun createEvent(@RequestBody event: Event): ResponseEntity<Event> {

        val createdUri: URI = linkTo(EventController::class.java)
                .slash("{id}")
                .toUri()

        event.id = 10

        // URI return을 원하는 경우 아래 return값 사용
        //return ResponseEntity.created(createdUri).build()
        return ResponseEntity.created(createdUri).body(event)
    }
}