package it.arcidiacono.warehouse.domain

import java.math.BigDecimal

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
    var availableStock: Int
) {
    fun reduceAvailabilityBy(amount: Int) {
        availableStock -= amount
    }
}

inline class ArticleIdentificationNumber(val value: Long)

data class AvailableProduct(
    val name: String,
    val price: Money,
    val availableQuantity: Int
)

data class Product2(
    val name: String,
    val price: Money,
    val billOfMaterials: List<Material2>
) {
    fun sellableQuantity(): Int =
        billOfMaterials.map { material ->
            material.article.availableStock / material.requiredAmount
        }.minOrNull()!!
}

data class Material2(
    val article: Article,
    val requiredAmount: Int
)