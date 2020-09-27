package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.AN_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT
import it.arcidiacono.warehouse.utils.stubArticlesRepositoryWith
import it.arcidiacono.warehouse.utils.stubProductsRepositoryWith
import org.junit.jupiter.api.Test

class SellProductUseCaseImplTest {

    private lateinit var sellProductsUseCaseImpl: SellProductUseCaseImpl

    @Test
    fun `happy path`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute(A_PRODUCT.name, 1).shouldBeRight()
    }

    @Test
    fun `try to sell too many items of a product`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute(A_PRODUCT.name, 4).shouldBeLeft(NotEnoughQuantity(1))
    }

    @Test
    fun `try to sell a non existent product`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute("aNonExistingProduct", 1).shouldBeLeft(
            NoMatchingProductFound("aNonExistingProduct")
        )
    }

    @Test
    fun `when no product is available`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(NoMatchingProductFound("whatever"))
    }

    @Test
    fun `when product repository fails`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Left(ProductRepositoryError))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(ProductRepositoryError)
    }

    @Test
    fun `when no article is available`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Right(listOf()))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(NoMatchingProductFound("whatever"))
    }

    @Test
    fun `when articles repository fails`() {
        val productsRepository: ProductsRepository = stubProductsRepositoryWith(Right(listOf()))
        val articlesRepository: ArticlesRepository = stubArticlesRepositoryWith(Left(ArticleRepositoryError))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(productsRepository, articlesRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(ArticleRepositoryError)
    }
}