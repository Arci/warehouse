package it.arcidiacono.warehouse.domain

import io.kotlintest.assertions.arrow.either.shouldBeRight
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FileProductsRepositoryTest {

    private lateinit var fileProductsRepository: FileProductsRepository

    @Test
    fun `happy path`() {
        fileProductsRepository = FileProductsRepository("/products.json")

        fileProductsRepository().shouldBeRight(
            listOf(
                Product(
                    name = "Dining Chair",
                    price = Money.euro(BigDecimal("12")),
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
                    price = Money.euro(BigDecimal("12")),
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
        fileProductsRepository = FileProductsRepository("/noProducts.json")

        fileProductsRepository().shouldBeRight(emptyList())
    }
}