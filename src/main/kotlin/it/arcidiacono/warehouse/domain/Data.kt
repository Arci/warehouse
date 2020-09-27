package it.arcidiacono.warehouse.domain

import java.math.BigDecimal

data class Product(
    val name: String,
    val price: Money,
    val articles: List<Article>
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
    val id: IdentificationNumber,
    val name: String,
    val availableStock: Int
)

inline class IdentificationNumber(val value: Long)