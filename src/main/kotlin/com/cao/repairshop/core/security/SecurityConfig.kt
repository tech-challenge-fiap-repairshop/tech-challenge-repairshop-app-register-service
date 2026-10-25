package com.cao.repairshop.core.security

import com.cao.repairshop.user.domain.entities.UserRole
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                // Public endpoints
                authorize(HttpMethod.POST, "/auth/login", permitAll)
                authorize(HttpMethod.POST, "/auth/register", permitAll)

                // Swagger / OpenAPI
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-custom.css", permitAll)
                authorize("/actuator/**", permitAll)

                // Customers and Vehicles: Attendant only
                authorize("/customers/**", hasRole(UserRole.ATTENDANT.name))
                authorize("/vehicles/**", hasRole(UserRole.ATTENDANT.name))

                // Service Orders: Customer can GET and APPROVE, Attendant can do everything
                authorize(HttpMethod.GET, "/service-orders/**", hasAnyRole(UserRole.ATTENDANT.name, UserRole.CUSTOMER.name))
                authorize(HttpMethod.POST, "/service-orders/*/approve", hasAnyRole(UserRole.ATTENDANT.name, UserRole.CUSTOMER.name))
                authorize("/service-orders/**", hasRole(UserRole.ATTENDANT.name))

                // Inventory/Insumes: Attendant only
                authorize("/insumes/**", hasRole(UserRole.ATTENDANT.name))

                // Invoices: Attendant only
                authorize("/invoices/**", hasRole(UserRole.ATTENDANT.name))

                // Default: authenticated
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(10)
}
