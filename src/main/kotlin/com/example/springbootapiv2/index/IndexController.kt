package com.example.springbootapiv2.index

import com.example.springbootapiv2.events.EventController
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.web.bind.annotation.GetMapping

class IndexController {
    inner class IndexClass : RepresentationModel<IndexClass>()

    @GetMapping("/api")
    fun index(): Any {
        val index = IndexClass()
        index.add(WebMvcLinkBuilder.linkTo(EventController::class.java)
                .slash("api")
                .slash("events")
                .withRel("events"))

        println(index)
        return index
    }
}

