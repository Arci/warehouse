package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.*
import java.math.BigDecimal

class JsonProductsRepository(
    private val datasource: Datasource
) : ProductsRepository {
    private val mapper = jacksonObjectMapper()

    override fun fetch(): Either<FailureReason, List<Product>> =
        datasource.read().flatMap {
            it.asJson().map { productsDao ->
                productsDao.toDomain()
            }
        }

    private fun String.asJson(): Either<FailureReason, ProductsDao> =
        try {
            Right(mapper.readValue(this))
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