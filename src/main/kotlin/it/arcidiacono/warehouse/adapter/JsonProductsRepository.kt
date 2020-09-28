package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import it.arcidiacono.warehouse.domain.Money
import it.arcidiacono.warehouse.domain.ProductsDeserializationError
import it.arcidiacono.warehouse.domain.RepositoryError
import java.math.BigDecimal

interface ProductsRepository {
    fun fetch(): Either<RepositoryError, List<ProductDto>>
}

data class ProductDto(
    val name: String,
    val price: Money,
    val billOfMaterials: List<MaterialDto>
)

data class MaterialDto(
    val articleId: ArticleIdentificationNumber,
    val requiredAmount: Int
)

class JsonProductsRepository(
    private val datasource: Datasource
) : ProductsRepository {
    private val mapper = jacksonObjectMapper()

    override fun fetch(): Either<RepositoryError, List<ProductDto>> =
        datasource.read().flatMap {
            it.asJson().map { productsDao ->
                productsDao.toDomain()
            }
        }

    private fun String.asJson(): Either<RepositoryError, ProductsDao> =
        try {
            Right(mapper.readValue(this))
        } catch (e: Exception) {
            Left(ProductsDeserializationError(e))
        }

    private fun ProductsDao.toDomain(): List<ProductDto> =
        this.products.map { productDao ->
            ProductDto(
                name = productDao.name,
                price = Money.euro(BigDecimal(42)),
                billOfMaterials = productDao.contain_articles.map { materialDao ->
                    MaterialDto(
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