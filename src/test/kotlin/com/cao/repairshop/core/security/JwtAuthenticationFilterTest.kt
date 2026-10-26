package com.cao.repairshop.core.security

import io.jsonwebtoken.Claims
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilterTest {

    private lateinit var jwtService: JwtService
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setup() {
        jwtService = mockk()
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtService)
        request = mockk(relaxed = true)
        response = mockk(relaxed = true)
        filterChain = mockk(relaxed = true)
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun teardown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should set authentication when token is valid`() {
        val token = "valid_token"
        val userId = "12345"
        val role = "ATTENDANT"
        val claims = mockk<Claims>()

        every { request.getHeader("Authorization") } returns "Bearer $token"
        every { jwtService.validateToken(token) } returns claims
        every { claims.subject } returns userId
        every { claims["role"] } returns role
        every { request.getAttribute(any()) } returns null

        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        assertEquals(userId, authentication!!.principal)
        assertEquals(1, authentication.authorities.size)
        assertEquals("ROLE_ATTENDANT", authentication.authorities.first().authority)

        verify { filterChain.doFilter(request, response) }
    }

    @Test
    fun `should not set authentication when token is invalid`() {
        val token = "invalid_token"

        every { request.getHeader("Authorization") } returns "Bearer $token"
        every { jwtService.validateToken(token) } returns null
        every { request.getAttribute(any()) } returns null

        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)

        verify { filterChain.doFilter(request, response) }
    }

    @Test
    fun `should not set authentication when no header is provided`() {
        every { request.getHeader("Authorization") } returns null
        every { request.getAttribute(any()) } returns null

        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)

        verify { filterChain.doFilter(request, response) }
    }
}
