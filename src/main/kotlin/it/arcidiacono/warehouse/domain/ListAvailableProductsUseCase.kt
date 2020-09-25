package it.arcidiacono.warehouse.domain

import java.math.BigDecimal
import arrow.core.Either

typealias ListAvailableProducts = () -> Either<FailureReason, List<Product>>

class ListAvailableProductsUseCase : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<Product>> {
        TODO("Not yet implemented")
    }
}

data class Product(
    val name: String,
    val price: BigDecimal,
    val articles: List<Articles>
)

class Articles(
    val identificationNumber: Number,
    val name: String,
    val availableStock: Number
)

sealed class FailureReason
