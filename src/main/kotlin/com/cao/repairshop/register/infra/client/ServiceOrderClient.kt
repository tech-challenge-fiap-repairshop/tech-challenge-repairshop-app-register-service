package com.cao.repairshop.register.infra.client

import com.cao.repairshop.register.domain.ServiceOrderExistenceChecker
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.UUID

data class CreateServiceOrderRequest(
    val customerId: UUID,
    val vehicleId: UUID,
    val description: String? = null
)

data class ServiceOrderResponse(
    val id: String,
    val customerId: UUID,
    val vehicleId: UUID,
    val status: String,
    val createdAt: String? = null
)

@Component
class ServiceOrderClient(
    @Value("\${repairshop.service-order.url:http://localhost:8080}")
    private val serviceOrderBaseUrl: String
) : ServiceOrderExistenceChecker {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val restTemplate: RestTemplate = RestTemplate()

    override fun existsByCustomerId(customerId: UUID): Boolean {
        val url = "$serviceOrderBaseUrl/service-orders?customerId=$customerId"
        return runCatching {
            val response = restTemplate.getForObject(url, String::class.java)
            response != null && response != "[]"
        }.onFailure { ex ->
            logger.warn("Falha ao checar ordens de serviço por cliente {}: {}", customerId, ex.message)
        }.getOrDefault(false)
    }

    override fun existsByVehicleId(vehicleId: UUID): Boolean {
        val url = "$serviceOrderBaseUrl/service-orders?vehicleId=$vehicleId"
        return runCatching {
            val response = restTemplate.getForObject(url, String::class.java)
            response != null && response != "[]"
        }.onFailure { ex ->
            logger.warn("Falha ao checar ordens de serviço por veículo {}: {}", vehicleId, ex.message)
        }.getOrDefault(false)
    }

    fun createServiceOrder(customerId: UUID, vehicleId: UUID, description: String?): ServiceOrderResponse? {
        val url = "$serviceOrderBaseUrl/service-orders"
        logger.info("Chamando síncrono Service Order Service: POST {} para customerId: {}", url, customerId)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = CreateServiceOrderRequest(
            customerId = customerId,
            vehicleId = vehicleId,
            description = description
        )

        return runCatching {
            val entity = HttpEntity(request, headers)
            val response = restTemplate.postForObject(url, entity, ServiceOrderResponse::class.java)
            logger.info("Retorno da criação da OS recebido com sucesso: {}", response?.id)
            response
        }.onFailure { ex ->
            logger.error("Falha ao invocar Service Order Service em {}: {}", url, ex.message)
        }.getOrNull()
    }
}
