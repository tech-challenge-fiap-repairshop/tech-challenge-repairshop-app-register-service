package com.cao.repairshop.core.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class SwaggerCssFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.endsWith("/swagger-ui/index.html")) {
            filterChain.doFilter(request, response)
            return
        }

        val wrapper = ContentCachingResponseWrapper(response)
        filterChain.doFilter(request, wrapper)

        val content = String(wrapper.contentAsByteArray)
        val modified = content
            .replace(
                "</head>",
                """
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
                <link rel="stylesheet" type="text/css" href="/swagger-custom.css" />
                </head>
                """.trimIndent()
            )
            .replace(
                "<body>",
                """
                <body>
                <div class="rs-header">
                    <h1>Repair <span>Shop</span></h1>
                    <p>Vehicle Repair Shop Management API</p>
                    <div class="rs-badges">
                        <span class="rs-badge amber">v1.0.0</span>
                        <span class="rs-badge blue">Spring Boot 4.x</span>
                        <span class="rs-badge">Kotlin</span>
                        <span class="rs-badge">PostgreSQL</span>
                        <span class="rs-badge green">JWT Auth</span>
                    </div>
                </div>
                """.trimIndent()
            )

        response.contentType = "text/html"
        response.setContentLength(modified.toByteArray().size)
        response.outputStream.write(modified.toByteArray())
        response.outputStream.flush()
    }
}
