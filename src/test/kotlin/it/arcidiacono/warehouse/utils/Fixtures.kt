package it.arcidiacono.warehouse.utils

import it.arcidiacono.warehouse.domain.*
import java.math.BigDecimal

object Fixtures {
    val A_PRICE = Money.euro(BigDecimal(42))
    val AN_ARTICLE = Article(
        id = ArticleIdentificationNumber(1),
        name = "anArticle",
        availableStock = 5
    )
    val ANOTHER_ARTICLE = Article(
        id = ArticleIdentificationNumber(2),
        name = "anotherArticle",
        availableStock = 10
    )
    val AN_UNAVAILABLE_ARTICLE = Article(
        id = ArticleIdentificationNumber(3),
        name = "anUnavailableArticle",
        availableStock = 0
    )
    val A_PRODUCT = Product2(
        name = "aProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material2(
                article = AN_ARTICLE,
                requiredAmount = 4
            )
        )
    )
    val ANOTHER_PRODUCT = Product2(
        name = "anotherProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material2(
                article = AN_ARTICLE,
                requiredAmount = 1
            ),
            Material2(
                article = ANOTHER_ARTICLE,
                requiredAmount = 3
            )
        )
    )
    val AN_UNAVAILABLE_PRODUCT = Product2(
        name = "anUnavailableProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material2(
                article = AN_ARTICLE,
                requiredAmount = 4
            ),
            Material2(
                article = ANOTHER_ARTICLE,
                requiredAmount = 12
            )
        )
    )
    val ANOTHER_UNAVAILABLE_PRODUCT = Product2(
        name = "anotherUnavailableProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material2(
                article = AN_UNAVAILABLE_ARTICLE,
                requiredAmount = 3
            )
        )
    )
}