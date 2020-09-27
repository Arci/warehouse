package it.arcidiacono.warehouse.domain

import arrow.core.Either
import java.math.BigDecimal

typealias ListAvailableProducts = () -> Either<FailureReason, List<Product>>
typealias ProductsRepository = () -> Either<FailureReason, List<Product>>

class ListAvailableProductsUseCase(
    private val productsRepository: ProductsRepository
) : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<Product>> =
        productsRepository().map { products ->
            products.filter { product ->
                product.articles.all { it.availableStock > 0 }
            }
        }
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
    val id: IdentificationNumber,
    val name: String,
    val availableStock: Int
)

inline class IdentificationNumber(val value: Long)

sealed class FailureReason
object GenericFailure : FailureReason()
