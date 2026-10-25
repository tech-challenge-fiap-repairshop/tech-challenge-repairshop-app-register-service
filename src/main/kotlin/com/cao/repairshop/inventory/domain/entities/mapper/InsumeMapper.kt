package com.cao.repairshop.inventory.domain.entities.mapper

import com.cao.repairshop.inventory.domain.entities.Insume
import com.cao.repairshop.inventory.infra.persistence.models.InsumeEntity
import com.cao.repairshop.inventory.infra.controller.dtos.InsumeResponse

fun Insume.toEntity() = InsumeEntity(
    id = id,
    name = name,
    brand = brand,
    skuId = skuId,
    quantity = quantity,
    price = price,
    unityPrice = unityPrice
)

fun InsumeEntity.toDomain() = Insume(
    id = id,
    name = name,
    brand = brand,
    skuId = skuId,
    quantity = quantity,
    price = price,
    unityPrice = unityPrice
)

fun Insume.toResponse() = InsumeResponse(
    id = id,
    name = name,
    brand = brand,
    skuId = skuId,
    quantity = quantity,
    price = price,
    unityPrice = unityPrice
)

