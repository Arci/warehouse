package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.Article
import it.arcidiacono.warehouse.domain.DatasourceError
import it.arcidiacono.warehouse.domain.FailureReason
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT_DTO
import it.arcidiacono.warehouse.utils.Fixtures.AN_ARTICLE
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT_DTO
import it.arcidiacono.warehouse.utils.stubArticlesRepositoryWith
import it.arcidiacono.warehouse.utils.stubProductsRepositoryWith
import org.junit.jupiter.api.Test

class WarehouseRepositoryImplTest {

    private lateinit var warehouseRepositoryImpl: WarehouseRepositoryImpl

    @Test
    fun `aggregates products and articles`() {
        warehouseRepositoryImpl = WarehouseRepositoryImpl(
            stubProductsRepositoryWith(Right(listOf(A_PRODUCT_DTO, ANOTHER_PRODUCT_DTO))),
            stubArticlesRepositoryWith(Right(listOf(AN_ARTICLE, ANOTHER_ARTICLE)))
        )

        warehouseRepositoryImpl.fetch().shouldBeRight(listOf(A_PRODUCT, ANOTHER_PRODUCT))
    }

    @Test
    fun `when product repository fails`() {
        val expectedError = DatasourceError(RuntimeException("something failed :("))
        warehouseRepositoryImpl = WarehouseRepositoryImpl(
            stubProductsRepositoryWith(Left(expectedError)),
            stubArticlesRepositoryWith(Right(emptyList()))
        )

        warehouseRepositoryImpl.fetch().shouldBeLeft(expectedError)
    }

    @Test
    fun `when articles repository fails`() {
        val expectedError = DatasourceError(RuntimeException("something failed :("))
        warehouseRepositoryImpl = WarehouseRepositoryImpl(
            stubProductsRepositoryWith(Right(emptyList())),
            stubArticlesRepositoryWith(Left(expectedError))
        )

        warehouseRepositoryImpl.fetch().shouldBeLeft(expectedError)
    }

    @Test
    fun `correctly updates articles on sell`() {
        val productsRepository = stubProductsRepositoryWith(Right(emptyList()))
        val articlesRepository = object : ArticlesRepository {
            override fun fetch(): Either<FailureReason, List<Article>> = Right(emptyList())

            override fun update(articles: List<Article>): Either<FailureReason, Unit> =
                if (articles == listOf(AN_ARTICLE.copy(availableStock = 1))) {
                    Right(Unit)
                } else {
                    Left(DatasourceError(RuntimeException("not the expected articles: $articles")))
                }
        }
        warehouseRepositoryImpl = WarehouseRepositoryImpl(
            productsRepository,
            articlesRepository
        )

        warehouseRepositoryImpl.sell(A_PRODUCT, 1).shouldBeRight(Unit)
    }
}