package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.*

class JsonArticlesRepository(
    private val datasource: Datasource
) : ArticlesRepository {
    private val mapper = jacksonObjectMapper()

    override fun fetch(): Either<FailureReason, List<Article>> =
        datasource.read().flatMap {
            it.asJson().map { articlesDao ->
                articlesDao.toDomain()
            }
        }

    override fun update(billOfMaterials: List<Material>, quantity: Int): Either<FailureReason, Unit> =
        fetch().flatMap { articles ->
            billOfMaterials.forEach { material ->
                val article = articles.find { it.id == material.articleId }!!
                article.reduceAvailabilityBy(material.requiredAmount * quantity)
            }

            val fileContent = mapper.writeValueAsString(articles.toRepresentation())
            datasource.write(fileContent)
        }

    private fun String.asJson(): Either<FailureReason, ArticlesDao> =
        try {
            Right(mapper.readValue(this))
        } catch (e: Exception) {
            Left(ArticlesDeserializationError(e))
        }

    private fun ArticlesDao.toDomain(): List<Article> =
        this.inventory.map {
            Article(
                id = ArticleIdentificationNumber(it.art_id),
                name = it.name,
                availableStock = it.stock
            )
        }

    private fun List<Article>.toRepresentation(): ArticlesDao =
        ArticlesDao(
            inventory = this.map {
                ArticleDao(
                    art_id = it.id.value,
                    name = it.name,
                    stock = it.availableStock
                )
            }
        )
}

private data class ArticlesDao(
    val inventory: List<ArticleDao>
)

private data class ArticleDao(
    val art_id: Long,
    val name: String,
    val stock: Int
)

