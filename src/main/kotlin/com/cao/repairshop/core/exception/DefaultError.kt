package com.cao.repairshop.core.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Standard error response")
data class DefaultError(
    @Schema(description = "Error timestamp", example = "2026-04-22T19:30:00Z")
    val timestamp: Instant,

    @Schema(description = "HTTP status code", example = "404")
    val status: Int,

    @Schema(description = "Error category", example = "Resource not found")
    val error: String,

    @Schema(description = "Detailed error message", example = "Customer not found.")
    val message: String?,

    @Schema(description = "Request path", example = "/customers/550e8400-e29b-41d4-a716-446655440000")
    val path: String
)
