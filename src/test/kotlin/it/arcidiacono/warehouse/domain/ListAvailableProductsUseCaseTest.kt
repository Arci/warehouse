package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import java.math.BigDecimal

private val AN_ARTICLE = Article(
    id = IdentificationNumber(1),
    name = "anArticle",
    availableStock = 10
)
private val ANOTHER_ARTICLE = Article(
    id = IdentificationNumber(2),
    name = "anotherArticle",
    availableStock = 10
)
private val AN_UNAVAILABLE_ARTICLE = Article(
    id = IdentificationNumber(3),
    name = "anUnavailableArticle",
    availableStock = 0
)

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
                        articles = listOf(AN_ARTICLE)
                    ),
                    Product(
                        name = "anotherProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("50.00")
                        ),
                        articles = listOf(AN_ARTICLE, ANOTHER_ARTICLE)
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
                    articles = listOf(AN_ARTICLE)
                ),
                Product(
                    name = "anotherProduct",
                    price = Money(
                        currency = "EUR",
                        amount = BigDecimal("50.00")
                    ),
                    articles = listOf(AN_ARTICLE, ANOTHER_ARTICLE)
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
                        articles = listOf(AN_ARTICLE)
                    ),
                    Product(
                        name = "anotherProduct",
                        price = Money(
                            currency = "EUR",
                            amount = BigDecimal("50.00")
                        ),
                        articles = listOf(AN_ARTICLE, ANOTHER_ARTICLE, AN_UNAVAILABLE_ARTICLE)
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
                    articles = listOf(AN_ARTICLE)
                )
            )
        )
    }

    @Test
    fun `when no product is available`() {
        val productsRepository: ProductsRepository = { Right(listOf()) }

        listAvailableProductsUseCase = ListAvailableProductsUseCase(productsRepository)

        listAvailableProductsUseCase().shouldBeRight(listOf())
    }

    @Test
    fun `when product repository fails`() {
        val productsRepository: ProductsRepository = { Left(GenericFailure) }

        listAvailableProductsUseCase = ListAvailableProductsUseCase(productsRepository)

        listAvailableProductsUseCase().shouldBeLeft(GenericFailure)
    }
}