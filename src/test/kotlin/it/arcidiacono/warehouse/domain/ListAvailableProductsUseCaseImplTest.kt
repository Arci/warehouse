package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.utils.stubArticlesRepositoryWith
import it.arcidiacono.warehouse.utils.stubProductsRepositoryWith
import org.junit.jupiter.api.Test
import java.math.BigDecimal

private val AN_ARTICLE = Article(
    id = ArticleIdentificationNumber(1),
    name = "anArticle",
    availableStock = 5
)
private val ANOTHER_ARTICLE = Article(
    id = ArticleIdentificationNumber(2),
    name = "anotherArticle",
    availableStock = 10
)
private val AN_UNAVAILABLE_ARTICLE = Article(
    id = ArticleIdentificationNumber(3),
    name = "anUnavailableArticle",
    availableStock = 0
)
private val A_PRODUCT = Product(
    name = "aProduct",
    price = Money.euro(BigDecimal("42.00")),
    billOfMaterials = listOf(
        Material(
            articleId = AN_ARTICLE.id,
            requiredAmount = 4
        )
    )
)
private val ANOTHER_PRODUCT = Product(
    name = "anotherProduct",
    price = Money.euro(BigDecimal("50.00")),
    billOfMaterials = listOf(
        Material(
            articleId = AN_ARTICLE.id,
            requiredAmount = 1
        ),
        Material(
            articleId = ANOTHER_ARTICLE.id,
            requiredAmount = 3
        )
    )
)
private val AN_UNAVAILABLE_PRODUCT = Product(
    name = "anUnavailableProduct",
    price = Money.euro(BigDecimal("36.00")),
    billOfMaterials = listOf(
        Material(
            articleId = AN_ARTICLE.id,
            requiredAmount = 4
        ),
        Material(
            articleId = ANOTHER_ARTICLE.id,
            requiredAmount = 12
        )
    )
)
private val ANOTHER_UNAVAILABLE_PRODUCT = Product(
    name = "anotherUnavailableProduct",
    price = Money.euro(BigDecimal("30.00")),
    billOfMaterials = listOf(
        Material(
            articleId = AN_UNAVAILABLE_ARTICLE.id,
            requiredAmount = 3
        )
    )
)

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