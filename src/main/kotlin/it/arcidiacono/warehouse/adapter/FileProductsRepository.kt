package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.*
import java.math.BigDecimal

class FileProductsRepository(
    private val productsFile: String
) : ProductsRepository {
    private val mapper = jacksonObjectMapper()

    override fun invoke(): Either<FailureReason, List<Product>> =
        try {
            val fileContent = FixtureLoader.readFile(productsFile)
            val productsDao = mapper.readValue<ProductsDao>(fileContent)
            Right(productsDao.toDomain())
        } catch (e: Exception) {
            Left(ThrowableFailure(e))
        }

    private fun ProductsDao.toDomain(): List<Product> =
        this.products.map { productDao ->
            Product(
                name = productDao.name,
                price = Money.euro(BigDecimal(42)),
                billOfMaterials = productDao.contain_articles.map { materialDao ->
                    Material(
                        articleId = ArticleIdentificationNumber(materialDao.art_id),
                        requiredAmount = materialDao.amount_of
                    )
                }
            )
        }
}

private data class ProductsDao(
    val products: List<ProductDao>
)

private data class ProductDao(
    val name: String,
    val contain_articles: List<MaterialDao>
)

private data class MaterialDao(
    val art_id: Long,
    val amount_of: Int
)