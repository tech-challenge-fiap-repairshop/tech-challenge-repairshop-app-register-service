package com.cao.repairshop.core.metrics

import com.cao.repairshop.inventory.infra.persistence.repositories.InsumeRepository
import com.cao.repairshop.payment.infra.persistence.repositories.InvoiceRepository
import com.cao.repairshop.register.infra.persistence.repositories.CustomerRepository
import com.cao.repairshop.register.infra.persistence.repositories.VehicleRepository
import com.cao.repairshop.serviceorder.domain.ServiceOrderStatus
import com.cao.repairshop.serviceorder.infra.persistence.repositories.ServiceOrderRepository
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BusinessMetricsBinderTest {

    private lateinit var customerRepository: CustomerRepository
    private lateinit var vehicleRepository: VehicleRepository
    private lateinit var serviceOrderRepository: ServiceOrderRepository
    private lateinit var invoiceRepository: InvoiceRepository
    private lateinit var insumeRepository: InsumeRepository
    private lateinit var registry: SimpleMeterRegistry
    private lateinit var binder: BusinessMetricsBinder

    @BeforeEach
    fun setup() {
        customerRepository = mockk()
        vehicleRepository = mockk()
        serviceOrderRepository = mockk()
        invoiceRepository = mockk()
        insumeRepository = mockk()
        registry = SimpleMeterRegistry()

        binder = BusinessMetricsBinder(
            customerRepository,
            vehicleRepository,
            serviceOrderRepository,
            invoiceRepository,
            insumeRepository
        )
    }

    @Test
    fun `should bind and evaluate all business gauges correctly`() {
        // Mocking repo values
        every { customerRepository.count() } returns 42L
        every { vehicleRepository.count() } returns 55L
        every { serviceOrderRepository.count() } returns 100L
        every { serviceOrderRepository.countByStatus(any()) } returns 10L
        every { serviceOrderRepository.getAverageStageDurationMinutes(any(), any()) } returns 15.5
        every { serviceOrderRepository.getAverageLeadTimeMinutes() } returns 180.0
        every { invoiceRepository.getTotalRevenue() } returns BigDecimal("150000.00")
        every { invoiceRepository.getAverageTicket() } returns BigDecimal("1500.00")
        every { invoiceRepository.count() } returns 80L
        every { insumeRepository.getTotalStockQuantity() } returns 500L
        every { insumeRepository.getTotalInventoryValue() } returns BigDecimal("35000.00")

        binder.bindTo(registry)

        // Asserts
        val customerGauge = registry.find("repairshop_business_customers_total").gauge()
        assertNotNull(customerGauge)
        assertEquals(42.0, customerGauge!!.value())

        val vehicleGauge = registry.find("repairshop_business_vehicles_total").gauge()
        assertNotNull(vehicleGauge)
        assertEquals(55.0, vehicleGauge!!.value())

        val soTotalGauge = registry.find("repairshop_business_service_orders_total").gauge()
        assertNotNull(soTotalGauge)
        assertEquals(100.0, soTotalGauge!!.value())

        val statusGauge = registry.find("repairshop_business_service_orders_by_status")
            .tag("status", ServiceOrderStatus.IN_EXECUTION.name).gauge()
        assertNotNull(statusGauge)
        assertEquals(10.0, statusGauge!!.value())

        val stageDurationGauge = registry.find("repairshop_business_stage_duration_minutes")
            .tag("from_status", "RECEIVED")
            .tag("to_status", "IN_DIAGNOSIS").gauge()
        assertNotNull(stageDurationGauge)
        assertEquals(15.5, stageDurationGauge!!.value())

        val leadTimeGauge = registry.find("repairshop_business_lead_time_minutes").gauge()
        assertNotNull(leadTimeGauge)
        assertEquals(180.0, leadTimeGauge!!.value())

        val revenueGauge = registry.find("repairshop_business_revenue_total_brl").gauge()
        assertNotNull(revenueGauge)
        assertEquals(150000.0, revenueGauge!!.value())

        val ticketGauge = registry.find("repairshop_business_average_ticket_brl").gauge()
        assertNotNull(ticketGauge)
        assertEquals(1500.0, ticketGauge!!.value())

        val stockGauge = registry.find("repairshop_business_inventory_items_total").gauge()
        assertNotNull(stockGauge)
        assertEquals(500.0, stockGauge!!.value())

        val stockValueGauge = registry.find("repairshop_business_inventory_value_total_brl").gauge()
        assertNotNull(stockValueGauge)
        assertEquals(35000.0, stockValueGauge!!.value())
    }

    @Test
    fun `should handle repository exceptions gracefully and return zero fallback`() {
        every { customerRepository.count() } throws RuntimeException("DB error")
        every { vehicleRepository.count() } throws RuntimeException("DB error")
        every { serviceOrderRepository.count() } throws RuntimeException("DB error")
        every { serviceOrderRepository.countByStatus(any()) } throws RuntimeException("DB error")
        every { serviceOrderRepository.getAverageStageDurationMinutes(any(), any()) } throws RuntimeException("DB error")
        every { serviceOrderRepository.getAverageLeadTimeMinutes() } throws RuntimeException("DB error")
        every { invoiceRepository.getTotalRevenue() } throws RuntimeException("DB error")
        every { invoiceRepository.getAverageTicket() } throws RuntimeException("DB error")
        every { invoiceRepository.count() } throws RuntimeException("DB error")
        every { insumeRepository.getTotalStockQuantity() } throws RuntimeException("DB error")
        every { insumeRepository.getTotalInventoryValue() } throws RuntimeException("DB error")

        binder.bindTo(registry)

        val customerGauge = registry.find("repairshop_business_customers_total").gauge()
        assertEquals(0.0, customerGauge!!.value())

        val revenueGauge = registry.find("repairshop_business_revenue_total_brl").gauge()
        assertEquals(0.0, revenueGauge!!.value())
    }
}
