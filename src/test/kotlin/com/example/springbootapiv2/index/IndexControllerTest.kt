package com.example.springbootapiv2.index

import com.example.springbootapiv2.common.BaseControllerTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

class IndexControllerTest : BaseControllerTest() {

    @Test
    fun index() {
        this.mockMvc.perform(get("/api"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_links.events").exists())
    }
}