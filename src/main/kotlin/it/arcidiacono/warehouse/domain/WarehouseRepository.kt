package it.arcidiacono.warehouse.domain

import arrow.core.Either

interface WarehouseRepository {
    fun fetch(): Either<FailureReason, List<Product>>
    fun sell(product: Product, quantity: Int): Either<FailureReason, Unit>
}