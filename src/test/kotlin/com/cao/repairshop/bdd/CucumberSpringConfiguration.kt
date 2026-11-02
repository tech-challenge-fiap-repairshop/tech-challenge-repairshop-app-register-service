package com.cao.repairshop.bdd

import com.cao.repairshop.RegisterServiceApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@CucumberContextConfiguration
@SpringBootTest(
    classes = [RegisterServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CucumberSpringConfiguration {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun registerInfrastructure(registry: DynamicPropertyRegistry) {
            val postgres = IntegrationContainers.postgres
            val redis = IntegrationContainers.redis

            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.data.redis.host") { redis.host }
            registry.add("spring.data.redis.port") { redis.firstMappedPort }
            registry.add("repairshop.service-order.url") { "http://127.0.0.1:1" }
        }
    }
}

private object IntegrationContainers {
    val postgres = PostgreSQLContainer<Nothing>("postgres:16").apply { start() }
    val redis = GenericContainer<Nothing>(DockerImageName.parse("redis:7.0-alpine")).apply {
        addExposedPort(6379)
        start()
    }
}
