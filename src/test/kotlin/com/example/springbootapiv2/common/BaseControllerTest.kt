package com.example.springbootapiv2.common

import com.fasterxml.jackson.databind.ObjectMapper
import jdk.nashorn.internal.ir.annotations.Ignore
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc // springBootTest 어노테이션을 쓰면서 MockMvc를 사용하려고 하면 @AutoConfigureMockMvc를 사용하야한다.
@AutoConfigureRestDocs // springRestDocs를 사용하기 위한 어노테이션
@Import(EventRestDocsMockMvcConfigurationCustomizer::class) // restDocs를 이쁘게 보이도록 해당 클래스 임포트해준다.
@ActiveProfiles("test")
@Ignore // test를 가지고 있는 클래스가 아니기 때문에 @Ignore를 붙여준다.
class BaseControllerTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper // content를 JSON으로 변경하기 위해서 사용

    @Autowired
    protected lateinit var modelMapper: ModelMapper

}