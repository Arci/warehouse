package it.arcidiacono.warehouse.domain

import arrow.core.Either

interface ProductsRepository {
    fun fetch(): Either<FailureReason, List<Product>>
}