package it.arcidiacono.warehouse.domain

import arrow.core.Either
import java.math.BigDecimal

typealias ProductsRepository = () -> Either<FailureReason, List<Product>>
typealias ArticlesRepository = () -> Either<FailureReason, List<Article>>

data class Product(
    val name: String,
    val price: Money,
    val billOfMaterials: List<Material>
)

data class Material(
    val articleId: ArticleIdentificationNumber,
    val requiredAmount: Int
)

data class Money(
    val currency: String,
    val amount: BigDecimal
) {
    companion object {
        fun euro(amount: BigDecimal) = Money("EUR", amount)
    }
}

data class Article(
    val id: ArticleIdentificationNumber,
    val name: String,
    val availableStock: Int
)

inline class ArticleIdentificationNumber(val value: Long)

data class AvailableProduct(
    val name: String,
    val price: Money,
    val availableQuantity: Int
)