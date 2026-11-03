package com.cao.repairshop.bdd

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.ScenarioScope
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Value
import tools.jackson.databind.json.JsonMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

@ScenarioScope
class RegisterServiceSteps {
    @Value("\${local.server.port}")
    private var serverPort: Int = 0

    private val httpClient = HttpClient.newHttpClient()
    private val json: JsonMapper = JsonMapper.builder().build()
    private var response: HttpResponse<String>? = null
    private var token: String? = null
    private lateinit var customerEmail: String
    private lateinit var customerDocument: String
    private var customerId: String? = null
    private var vehicleId: String? = null

    @Before
    fun resetScenarioState() {
        response = null
        token = null
        customerId = null
        vehicleId = null
    }

    @Given("an authenticated attendant")
    fun authenticateAttendant() {
        val sequence = userSequence.incrementAndGet()
        val cpf = cpfFor(sequence)
        customerEmail = "attendant-$sequence@example.test"
        customerDocument = cpfFor(sequence + 1_000_000)
        val password = "securePass123"

        send(
            "POST",
            "/auth/register",
            mapOf(
                "name" to "BDD Attendant $sequence",
                "function" to "ATTENDANT",
                "cpf" to cpf,
                "email" to customerEmail,
                "phone" to "11999887766",
                "password" to password
            ),
            authenticated = false
        )
        assertStatus(201)

        send(
            "POST",
            "/auth/login",
            mapOf("cpf" to cpf, "password" to password),
            authenticated = false
        )
        assertStatus(200)
        token = bodyJson().get("token").asText()
    }

    @When("the attendant registers a customer")
    fun registerCustomer() {
        postCustomer(customerDocument)
    }

    @When("the attendant registers the same customer document again")
    fun registerDuplicateCustomer() {
        postCustomer(customerDocument, name = "Duplicate Customer")
    }

    @When("the attendant submits a customer with a blank name")
    fun submitCustomerWithBlankName() {
        send(
            "POST",
            "/customers",
            mapOf("name" to "", "document" to customerDocument, "email" to customerEmail)
        )
    }

    @When("the attendant registers a vehicle for that customer")
    fun registerVehicle() {
        send(
            "POST",
            "/vehicles",
            mapOf(
                "customerEmail" to customerEmail,
                "plate" to "BDD1D23",
                "brand" to "Toyota",
                "model" to "Corolla",
                "color" to "Silver",
                "manufacturingDate" to "2022-06-15"
            )
        )
        if (response?.statusCode() == 201) {
            vehicleId = bodyJson().get("id").asText()
        }
    }

    @When("the attendant retrieves the registered customer")
    fun retrieveCustomer() {
        send("GET", "/customers/${requireNotNull(customerId)}")
    }

    @When("the attendant retrieves the registered vehicle")
    fun retrieveVehicle() {
        send("GET", "/vehicles/${requireNotNull(vehicleId)}")
    }

    @When("the attendant requests a customer that does not exist")
    fun retrieveMissingCustomer() {
        send("GET", "/customers/${UUID.randomUUID()}")
    }

    @When("an unauthenticated caller requests the customer list")
    fun requestCustomersWithoutAuthentication() {
        send("GET", "/customers", authenticated = false)
    }

    @Then("the response status is {int}")
    fun responseStatusIs(expected: Int) {
        assertStatus(expected)
    }

    @Then("the response contains customer name {string}")
    fun responseContainsCustomerName(name: String) {
        assertEquals(name, bodyJson().get("name").asText())
    }

    @Then("the response contains a customer identifier")
    fun responseContainsCustomerId() {
        assertTrue(bodyJson().get("id").asText().isNotBlank())
    }

    @Then("the vehicle response belongs to the registered customer")
    fun vehicleBelongsToCustomer() {
        assertEquals(customerId, bodyJson().get("customerId").asText())
    }

    @Then("the response error is {string}")
    fun responseErrorIs(error: String) {
        assertEquals(error, bodyJson().get("error").asText())
    }

    private fun postCustomer(document: String, name: String = "BDD Customer") {
        send(
            "POST",
            "/customers",
            mapOf(
                "name" to name,
                "document" to document,
                "email" to customerEmail,
                "phone" to "11999887766",
                "birthDate" to "1990-05-20"
            )
        )
        if (response?.statusCode() == 201) {
            customerId = bodyJson().get("id").asText()
        }
    }

    private fun send(
        method: String,
        path: String,
        payload: Any? = null,
        authenticated: Boolean = true
    ) {
        val builder = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:$serverPort$path"))
            .header("Accept", "application/json")
        if (payload != null) {
            builder.header("Content-Type", "application/json")
        }
        if (authenticated) {
            token?.let { builder.header("Authorization", "Bearer $it") }
        }

        val body = payload?.let(json::writeValueAsString) ?: ""
        val request = when (method) {
            "GET" -> builder.GET().build()
            "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body)).build()
            else -> error("Unsupported test HTTP method: $method")
        }
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun assertStatus(expected: Int) {
        assertEquals(expected, requireNotNull(response).statusCode(), response?.body())
    }

    private fun bodyJson() = json.readTree(requireNotNull(response).body())

    private fun cpfFor(sequence: Int): String {
        val base = (100_000_000L + sequence).toString().takeLast(9)
        val digits = base.map(Char::digitToInt)
        val firstCheckDigit = checkDigit(digits, 10)
        val secondCheckDigit = checkDigit(digits + firstCheckDigit, 11)
        return "$base$firstCheckDigit$secondCheckDigit"
    }

    private fun checkDigit(digits: List<Int>, initialWeight: Int): Int {
        val sum = digits.mapIndexed { index, digit -> digit * (initialWeight - index) }.sum()
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    companion object {
        private val userSequence = AtomicInteger()
    }
}
