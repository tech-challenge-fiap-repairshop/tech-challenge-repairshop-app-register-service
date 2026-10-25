package com.cao.repairshop.inventory.infra.persistence.repositories

import com.cao.repairshop.inventory.infra.persistence.models.InsumeEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface InsumeRepository : JpaRepository<InsumeEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InsumeEntity i WHERE i.id = :id")
    fun findByIdForUpdate(id: UUID): InsumeEntity?

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM InsumeEntity i")
    fun getTotalStockQuantity(): Long?

    @Query("SELECT COALESCE(SUM(CAST(i.quantity AS bigdecimal) * i.price), 0) FROM InsumeEntity i")
    fun getTotalInventoryValue(): java.math.BigDecimal?
}

