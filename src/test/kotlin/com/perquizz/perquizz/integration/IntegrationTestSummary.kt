package com.perquizz.perquizz.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.perquizz.perquizz.auth.services.TokenEmitterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTestSummary {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var tokenEmitterService: TokenEmitterService
}
