package com.example.springbootapiv2.index

import com.example.springbootapiv2.events.EventController
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    inner class IndexClass : RepresentationModel<IndexClass>()

    @GetMapping("/api")
    fun index(): IndexController.IndexClass {
        val index: IndexController.IndexClass = IndexClass()
        return index.add(WebMvcLinkBuilder.linkTo(EventController::class.java)
                .slash("api")
                .slash("events")
                .withRel("events"))
    }
}

