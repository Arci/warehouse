package it.arcidiacono.warehouse.utils

import it.arcidiacono.warehouse.adapter.MaterialDto
import it.arcidiacono.warehouse.adapter.ProductDto
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
    val A_PRODUCT_DTO = ProductDto(
        name = "aProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            MaterialDto(
                articleId = AN_ARTICLE.id,
                requiredAmount = 4
            )
        )
    )
    val A_PRODUCT = Product(
        name = "aProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material(
                article = AN_ARTICLE,
                requiredAmount = 4
            )
        )
    )
    val ANOTHER_PRODUCT_DTO = ProductDto(
        name = "anotherProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            MaterialDto(
                articleId = AN_ARTICLE.id,
                requiredAmount = 1
            ),
            MaterialDto(
                articleId = ANOTHER_ARTICLE.id,
                requiredAmount = 3
            )
        )
    )
    val ANOTHER_PRODUCT = Product(
        name = "anotherProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material(
                article = AN_ARTICLE,
                requiredAmount = 1
            ),
            Material(
                article = ANOTHER_ARTICLE,
                requiredAmount = 3
            )
        )
    )
    val AN_UNAVAILABLE_PRODUCT = Product(
        name = "anUnavailableProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material(
                article = AN_ARTICLE,
                requiredAmount = 4
            ),
            Material(
                article = ANOTHER_ARTICLE,
                requiredAmount = 12
            )
        )
    )
    val ANOTHER_UNAVAILABLE_PRODUCT = Product(
        name = "anotherUnavailableProduct",
        price = A_PRICE,
        billOfMaterials = listOf(
            Material(
                article = AN_UNAVAILABLE_ARTICLE,
                requiredAmount = 3
            )
        )
    )
}