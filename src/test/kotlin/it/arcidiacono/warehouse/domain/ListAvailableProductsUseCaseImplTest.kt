package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_UNAVAILABLE_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.AN_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.AN_UNAVAILABLE_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.AN_UNAVAILABLE_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT
import it.arcidiacono.warehouse.utils.stubArticlesRepositoryWith
import it.arcidiacono.warehouse.utils.stubProductsRepositoryWith
import org.junit.jupiter.api.Test

class ListAvailableProductsUseCaseImplTest {

    private lateinit var listAvailableProductsUseCaseImpl: ListAvailableProductsUseCaseImpl

    @Test
    fun `list available products with their quantities`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = A_PRODUCT.name,
                    price = A_PRODUCT.price,
                    availableQuantity = 1
                ),
                AvailableProduct(
                    name = ANOTHER_PRODUCT.name,
                    price = ANOTHER_PRODUCT.price,
                    availableQuantity = 3
                )
            )
        )
    }

    @Test
    fun `when a product is not available is not listed`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(
            Right(
                listOf(
                    A_PRODUCT,
                    AN_UNAVAILABLE_PRODUCT,
                    ANOTHER_UNAVAILABLE_PRODUCT
                )
            )
        )
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(
            Right(
                listOf(
                    AN_ARTICLE,
                    ANOTHER_ARTICLE,
                    AN_UNAVAILABLE_ARTICLE
                )
            )
        )
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = A_PRODUCT.name,
                    price = A_PRODUCT.price,
                    availableQuantity = 1
                )
            )
        )
    }

    @Test
    fun `when no product is available`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(listOf())
    }

    @Test
    fun `when product repository fails`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Left(ProductRepositoryError))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeLeft(ProductRepositoryError)
    }

    @Test
    fun `when no article is available`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(listOf())
    }

    @Test
    fun `when articles repository fails`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Left(ArticleRepositoryError))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(productsRepository, articlesRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeLeft(ArticleRepositoryError)
    }
}