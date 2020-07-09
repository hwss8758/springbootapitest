package com.example.springbootapiv2.common

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestComponent
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint

@TestComponent
class EventRestDocsMockMvcConfigurationCustomizer : RestDocsMockMvcConfigurationCustomizer {
    override fun customize(configurer: MockMvcRestDocumentationConfigurer?) {
        configurer?.operationPreprocessors()?.withRequestDefaults(prettyPrint())?.withResponseDefaults(prettyPrint())
    }
}