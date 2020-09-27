package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal
import java.nio.charset.StandardCharsets

typealias ProductsRepository = () -> Either<FailureReason, List<Product>>
typealias ArticlesRepository = () -> Either<FailureReason, List<Article>>

class FileProductsRepository(
    private val productsFile: String
) : ProductsRepository {
    private val mapper = jacksonObjectMapper()

    override fun invoke(): Either<FailureReason, List<Product>> =
        try {
            val productsFile = FixtureLoader.readFile(productsFile)
            val productsDao = mapper.readValue<ProductsDao>(productsFile)
            Right(productsDao.toDomain())
        } catch (e: Exception) {
            Left(ThrowableFailure(e))
        }

    private fun ProductsDao.toDomain(): List<Product> =
        this.products.map { productDao ->
            Product(
                name = productDao.name,
                price = Money.euro(BigDecimal("12")), // TODO
                billOfMaterials = productDao.contain_articles.map { materialDao ->
                    Material(
                        articleId = ArticleIdentificationNumber(materialDao.art_id),
                        requiredAmount = materialDao.amount_of
                    )
                }
            )
        }
}

data class ArticlesDao(
    val inventory: List<ArticleDao>
)

data class ArticleDao(
    val art_id: Int,
    val name: String,
    val stock: Int
)

data class ProductsDao(
    val products: List<ProductDao>
)

data class ProductDao(
    val name: String,
    val contain_articles: List<MaterialDao>
)

data class MaterialDao(
    val art_id: Long,
    val amount_of: Int
)

object FixtureLoader {
    fun readFile(path: String): String = this.javaClass.getResource(path).readText(StandardCharsets.UTF_8)
}