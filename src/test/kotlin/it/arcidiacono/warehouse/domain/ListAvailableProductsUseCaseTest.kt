package it.arcidiacono.warehouse.domain

import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ListAvailableProductsUseCaseTest {

    private lateinit var listAvailableProductsUseCase: ListAvailableProductsUseCase

    @Test
    fun `list all products`() {
        val productsRepository: ProductsRepository = {
            Right(
                listOf(
                    Product(
                        name = "aProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("42.00")
                        ),
                        articles = listOf(
                            Article(
                                identificationNumber = 1,
                                name = "anArticle",
                                availableStock = 10
                            )
                        )
                    ),
                    Product(
                        name = "anotherProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("50.00")
                        ),
                        articles = listOf(
                            Article(
                                identificationNumber = 1,
                                name = "anArticle",
                                availableStock = 10
                            ),
                            Article(
                                identificationNumber = 1,
                                name = "anotherArticle",
                                availableStock = 10
                            )
                        )
                    )
                )
            )
        }
        listAvailableProductsUseCase = ListAvailableProductsUseCase(productsRepository)

        listAvailableProductsUseCase().shouldBeRight(
            listOf(
                Product(
                    name = "aProduct",
                    price = Money(
                        currency = "EUR",
                        amount = BigDecimal("42.00")
                    ),
                    articles = listOf(
                        Article(
                            identificationNumber = 1,
                            name = "anArticle",
                            availableStock = 10
                        )
                    )
                ),
                Product(
                    name = "anotherProduct",
                    price = Money(
                        currency = "EUR",
                        amount = BigDecimal("50.00")
                    ),
                    articles = listOf(
                        Article(
                            identificationNumber = 1,
                            name = "anArticle",
                            availableStock = 10
                        ),
                        Article(
                            identificationNumber = 1,
                            name = "anotherArticle",
                            availableStock = 10
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `when a product is not available is not listed`() {
        val productsRepository: ProductsRepository = {
            Right(
                listOf(
                    Product(
                        name = "aProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("42.00")
                        ),
                        articles = listOf(
                            Article(
                                identificationNumber = 1,
                                name = "anArticle",
                                availableStock = 10
                            )
                        )
                    ),
                    Product(
                        name = "anotherProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("50.00")
                        ),
                        articles = listOf(
                            Article(
                                identificationNumber = 1,
                                name = "anArticle",
                                availableStock = 10
                            ),
                            Article(
                                identificationNumber = 1,
                                name = "anUnavailableItem",
                                availableStock = 0
                            )
                        )
                    )
                )
            )
        }
        listAvailableProductsUseCase = ListAvailableProductsUseCase(productsRepository)

        listAvailableProductsUseCase().shouldBeRight(
            listOf(
                Product(
                    name = "aProduct",
                    price = Money(
                        currency = "EUR",
                        amount = BigDecimal("42.00")
                    ),
                    articles = listOf(
                        Article(
                            identificationNumber = 1,
                            name = "anArticle",
                            availableStock = 10
                        )
                    )
                )
            )
        )
    }

    @Test
    @Disabled
    fun `when no product is available`() {
        TODO("Not yet implemented")
    }
}