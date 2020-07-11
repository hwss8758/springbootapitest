package com.example.springbootapiv2.index

import com.example.springbootapiv2.common.EventRestDocsMockMvcConfigurationCustomizer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@SpringBootTest
@AutoConfigureMockMvc // springBootTest 어노테이션을 쓰면서 MockMvc를 사용하려고 하면 @AutoConfigureMockMvc를 사용하야한다.
@AutoConfigureRestDocs // springRestDocs를 사용하기 위한 어노테이션
@Import(EventRestDocsMockMvcConfigurationCustomizer::class) // restDocs를 이쁘게 보이도록 해당 클래스 임포트해준다.
@ActiveProfiles("test")
class IndexControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun index() {
        this.mockMvc.perform(get("/api"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_links.events").exists())
    }
}