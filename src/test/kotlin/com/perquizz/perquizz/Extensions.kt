package com.perquizz.perquizz

import com.perquizz.perquizz.auth.valueobjects.Token
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun MockHttpServletRequestBuilder.withAuthToken(token: Token): MockHttpServletRequestBuilder =
    this.header("Authorization", "Bearer ${token.value}")
