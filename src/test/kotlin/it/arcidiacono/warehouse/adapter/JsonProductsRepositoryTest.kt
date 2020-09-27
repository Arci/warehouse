package it.arcidiacono.warehouse.adapter

import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import it.arcidiacono.warehouse.domain.Material
import it.arcidiacono.warehouse.domain.Money
import it.arcidiacono.warehouse.domain.Product
import it.arcidiacono.warehouse.utils.inMemoryDatasource
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

private val A_PRICE = Money.euro(BigDecimal(42))

class JsonProductsRepositoryTest {

    private lateinit var jsonProductsRepository: JsonProductsRepository

    @Test
    fun `happy path`() {
        jsonProductsRepository = JsonProductsRepository(inMemoryDatasource("/products/products.json"))

        jsonProductsRepository.fetch().shouldBeRight(
            listOf(
                Product(
                    name = "Dining Chair",
                    price = A_PRICE,
                    billOfMaterials = listOf(
                        Material(
                            articleId = ArticleIdentificationNumber(1),
                            requiredAmount = 4
                        ),
                        Material(
                            articleId = ArticleIdentificationNumber(2),
                            requiredAmount = 8
                        )
                    )
                ),
                Product(
                    name = "Dinning Table",
                    price = A_PRICE,
                    billOfMaterials = listOf(
                        Material(
                            articleId = ArticleIdentificationNumber(1),
                            requiredAmount = 4
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `when file does not exists`() {
        jsonProductsRepository = JsonProductsRepository(inMemoryDatasource("whatever"))

        assertTrue(jsonProductsRepository.fetch().isLeft())
    }

    @Test
    fun `when json is not valid`() {
        jsonProductsRepository = JsonProductsRepository(inMemoryDatasource("invalid.json"))

        assertTrue(jsonProductsRepository.fetch().isLeft())
    }

    @Test
    fun `when no product specified`() {
        jsonProductsRepository = JsonProductsRepository(inMemoryDatasource("/products/noProducts.json"))

        jsonProductsRepository.fetch().shouldBeRight(emptyList())
    }
}