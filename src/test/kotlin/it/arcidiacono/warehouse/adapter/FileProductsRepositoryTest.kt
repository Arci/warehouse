package it.arcidiacono.warehouse.adapter

import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import it.arcidiacono.warehouse.domain.Material
import it.arcidiacono.warehouse.domain.Money
import it.arcidiacono.warehouse.domain.Product
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

private val A_PRICE = Money.euro(BigDecimal(42))

class FileProductsRepositoryTest {

    private lateinit var fileProductsRepository: FileProductsRepository

    @Test
    fun `happy path`() {
        fileProductsRepository = FileProductsRepository("/products/products.json")

        fileProductsRepository().shouldBeRight(
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
        fileProductsRepository = FileProductsRepository("wathever")

        assertTrue(fileProductsRepository().isLeft())
    }

    @Test
    fun `when no product specified`() {
        fileProductsRepository = FileProductsRepository("/products/noProducts.json")

        fileProductsRepository().shouldBeRight(emptyList())
    }
}