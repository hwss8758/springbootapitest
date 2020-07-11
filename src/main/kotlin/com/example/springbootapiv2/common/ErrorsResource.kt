package com.example.springbootapiv2.common

import com.example.springbootapiv2.index.IndexController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.validation.Errors


class ErrorsResource(var errors: Errors, vararg links: Link?) : EntityModel<Errors?>() {
    init {
        add(WebMvcLinkBuilder.linkTo(IndexController::class.java).withRel("index"))
    }
}