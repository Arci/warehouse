package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
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

class SellProductUseCaseTest {

    private lateinit var sellProductsUseCase: SellProductUseCase

    @Test
    fun `happy path`() {
        val productsRepository: ProductsRepository = { Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)) }
        val articlesRepository: ArticlesRepository = { Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("aProduct", 1).shouldBeRight()
    }

    @Test
    fun `try to sell a non existent product`() {
        val productsRepository: ProductsRepository = { Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)) }
        val articlesRepository: ArticlesRepository = { Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("aNonExistingProduct", 1).shouldBeLeft(NoMatchingProductFound)
    }

    @Test
    fun `try to sell too many items of a product`() {
        val productsRepository: ProductsRepository = { Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)) }
        val articlesRepository: ArticlesRepository = { Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("aProduct", 4).shouldBeLeft(NotEnoughQuantity(1))
    }

    @Test
    fun `when no product is available`() {
        val productsRepository: ProductsRepository = { Right(listOf()) }
        val articlesRepository: ArticlesRepository = { Right(listOf()) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("wathever", 1).shouldBeLeft(NoMatchingProductFound)
    }

    @Test
    fun `when product repository fails`() {
        val productsRepository: ProductsRepository = { Left(ProductRepositoryError) }
        val articlesRepository: ArticlesRepository = { Right(listOf()) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("wathever", 1).shouldBeLeft(ProductRepositoryError)
    }

    @Test
    fun `when no article is available`() {
        val productsRepository: ProductsRepository = { Right(listOf()) }
        val articlesRepository: ArticlesRepository = { Right(listOf()) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("wathever", 1).shouldBeLeft(NoMatchingProductFound)
    }

    @Test
    fun `when articles repository fails`() {
        val productsRepository: ProductsRepository = { Right(listOf()) }
        val articlesRepository: ArticlesRepository = { Left(ArticleRepositoryError) }
        val updateArticles: UpdateArticles = { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }

        sellProductsUseCase = SellProductUseCase(productsRepository, articlesRepository, updateArticles)

        sellProductsUseCase("wathever", 1).shouldBeLeft(ArticleRepositoryError)
    }
}