package it.arcidiacono.warehouse.domain

import java.math.BigDecimal

data class AvailableProduct(
    val name: String,
    val price: Money,
    val availableQuantity: Int
)

data class Product(
    val name: String,
    val price: Money,
    val billOfMaterials: List<Material>
) {
    fun sellableQuantity(): Int =
        billOfMaterials.map { material ->
            material.article.availableStock / material.requiredAmount
        }.minOrNull()!!
}

data class Money(
    val currency: String,
    val amount: BigDecimal
) {
    companion object {
        fun euro(amount: BigDecimal) = Money("EUR", amount)
    }
}

data class Material(
    val article: Article,
    val requiredAmount: Int
)

data class Article(
    val id: ArticleIdentificationNumber,
    val name: String,
    var availableStock: Int
) {
    fun reduceAvailabilityBy(amount: Int) {
        availableStock -= amount
    }
}

inline class ArticleIdentificationNumber(val value: Long)
