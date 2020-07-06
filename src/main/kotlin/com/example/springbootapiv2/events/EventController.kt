package com.example.springbootapiv2.events

import org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import java.net.URI

@Controller
class EventController {

    @PostMapping("/api/events")
    fun createEvent(): ResponseEntity<URI> {

        val createdUri: URI = linkTo(methodOn(EventController::class.java).createEvent())
                .slash("{id}")
                .toUri()

        return ResponseEntity.created(createdUri).build()
    }
}