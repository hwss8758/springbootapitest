package com.example.springbootapiv2.events

import com.example.springbootapiv2.index.IndexController
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

data class EventResourcePaged(@JsonUnwrapped var event: Event) : RepresentationModel<EventResourcePaged>() {
    init {
        add(WebMvcLinkBuilder.linkTo(IndexController::class.java)
                .slash("api")
                .slash("events")
                .slash(event.id)
                .withSelfRel())
    }
}