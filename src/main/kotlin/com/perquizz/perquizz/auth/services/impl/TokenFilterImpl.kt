package com.perquizz.perquizz.auth.services.impl

import com.perquizz.perquizz.auth.services.TokenReaderService
import com.perquizz.perquizz.auth.valueobjects.Token
import com.perquizz.perquizz.exceptions.InvalidTokenException
import com.perquizz.perquizz.users.entities.UserEntity
import com.perquizz.perquizz.users.repositories.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenFilterImpl(
    private val userRepository: UserRepository,
    private val tokenReaderService: TokenReaderService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        getUserFrom(request)?.let {
            val authToken = UsernamePasswordAuthenticationToken(it, null, listOf())
            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }

    private fun getUserFrom(request: HttpServletRequest): UserEntity? =
        getTokenFrom(request)?.let {
            try {
                tokenReaderService.readToken(it).userId
            } catch (_: InvalidTokenException) {
                null
            }
        }?.let {
            userRepository.findById(it).orElse(null)
        }

    private fun getTokenFrom(request: HttpServletRequest): Token? =
        request.getHeader("Authorization")?.let {
            if (it.startsWith("Bearer ")) {
                it.replace("Bearer ", "")
            } else {
                null
            }
        }?.let {
            Token(it)
        }
}
