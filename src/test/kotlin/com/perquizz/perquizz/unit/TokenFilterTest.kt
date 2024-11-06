package com.perquizz.perquizz.unit

import com.perquizz.perquizz.auth.dtos.TokenRequestDto
import com.perquizz.perquizz.auth.services.impl.TokenEmitterServiceImpl
import com.perquizz.perquizz.auth.services.impl.TokenFilterImpl
import com.perquizz.perquizz.auth.services.impl.TokenReaderServiceImpl
import com.perquizz.perquizz.builders.buildUser
import com.perquizz.perquizz.configuration.JwtConfiguration
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant
import java.util.Optional

class TokenFilterTest {
    private val jwtConfiguration =
        JwtConfiguration(
            "testKey",
            3600L,
        )
    private val tokenEmitterService = TokenEmitterServiceImpl(jwtConfiguration)
    private val tokenReaderService = TokenReaderServiceImpl(jwtConfiguration)
    private val request: HttpServletRequest = mockk()
    private val response: HttpServletResponse = mockk()
    private val filterChain: FilterChain = mockk()
    private val userRepository: UserRepository = mockk()
    private val tokenFilter = TokenFilterImpl(userRepository, tokenReaderService)

    @BeforeEach
    fun setUp() {
        SecurityContextHolder
            .getContext()
            .authentication = null
    }

    @Test
    fun `should set user authentication when token is valid`() {
        val user = buildUser { id = 1 }

        requestIsPrepared()
        authorizationIs("Bearer ${token()}")
        filterChainExecutes()

        every { userRepository.findById(1) } returns Optional.of(user)

        tokenFilter.doFilter(request, response, filterChain)

        assertThat(authenticatedEntity())
            .isInstanceOf(UserEntity::class.java)
            .usingRecursiveComparison()
            .isEqualTo(user)

        verify {
            userRepository.findById(1)
            filterChain.doFilter(request, response)
        }
    }

    @Test
    fun `should not set user authentication when user is not in the database`() {
        requestIsPrepared()
        authorizationIs("Bearer ${token()}")

        every { userRepository.findById(1) } returns Optional.empty()
        every { response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token") } just runs
        every { filterChain.doFilter(request, response) } just runs

        tokenFilter.doFilter(request, response, filterChain)

        assertThat(authenticatedEntity()).isNull()
    }

    @Test
    fun `should not set user authentication when token is invalid`() {
        requestIsPrepared()
        authorizationIs("Bearer definitelyNotAToken")

        every { response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token") } just runs
        every { filterChain.doFilter(request, response) } just runs

        tokenFilter.doFilter(request, response, filterChain)

        assertThat(authenticatedEntity()).isNull()
    }

    @Test
    fun `should not authenticate user when no token is present`() {
        requestIsPrepared()
        filterChainExecutes()
        authorizationIs(null)

        tokenFilter.doFilter(request, response, filterChain)

        assertThat(authenticatedEntity()).isNull()
    }

    @Test
    fun `should not authenticate user when prefix is not Bearer`() {
        requestIsPrepared()
        filterChainExecutes()
        authorizationIs("Other ${token()}")

        tokenFilter.doFilter(request, response, filterChain)

        assertThat(authenticatedEntity()).isNull()
    }

    private fun token(): String =
        tokenEmitterService
            .issueToken(TokenRequestDto(1L), Instant.now()).value

    private fun requestIsPrepared() {
        every { request.getAttribute(any()) } returns null
        every { request.dispatcherType } returns null
        every { request.setAttribute(any(), any()) } just runs
        every { request.removeAttribute(any()) } just runs
    }

    private fun authorizationIs(value: String?) {
        every { request.getHeader("Authorization") } returns value
    }

    private fun filterChainExecutes() {
        every { filterChain.doFilter(request, response) } just runs
    }

    private fun authenticatedEntity(): Any? =
        SecurityContextHolder
            .getContext()
            .authentication
            ?.principal
}
