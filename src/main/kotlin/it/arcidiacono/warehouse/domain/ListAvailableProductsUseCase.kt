package it.arcidiacono.warehouse.domain

import arrow.core.Either
import java.math.BigDecimal

typealias ListAvailableProducts = () -> Either<FailureReason, List<Product>>
typealias ProductsRepository = () -> Either<FailureReason, List<Product>>

class ListAvailableProductsUseCase(
    private val productsRepository: ProductsRepository
) : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<Product>> = productsRepository()
}

data class Product(
    val name: String,
    val price: Money,
    val articles: List<Article>
)

data class Money(
    val currency: String,
    val amount: BigDecimal
)

data class Article(
    val identificationNumber: Number,
    val name: String,
    val availableStock: Number
)

sealed class FailureReason
