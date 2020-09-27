package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.*

interface ArticlesRepository {
    fun fetch(): Either<FailureReason, List<Article>>
    fun update(articles: List<Article>): Either<FailureReason, Unit>
}

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

    override fun update(articles: List<Article>): Either<FailureReason, Unit> =
        fetch().flatMap { allArticles ->
            val updatedArticles = allArticles.map { oldArticle ->
                val newArticle = articles.find { it.id == oldArticle.id }
                newArticle ?: oldArticle
            }
            writeAsJson(updatedArticles.toRepresentation())
        }

    private fun String.asJson(): Either<FailureReason, InventoryDao> =
        try {
            Right(mapper.readValue(this))
        } catch (e: Exception) {
            Left(ArticlesDeserializationError(e))
        }

    private fun InventoryDao.toDomain(): List<Article> =
        this.inventory.map {
            Article(
                id = ArticleIdentificationNumber(it.art_id),
                name = it.name,
                availableStock = it.stock
            )
        }

    private fun List<Article>.toRepresentation(): InventoryDao =
        InventoryDao(
            inventory = this.map {
                ArticleDao(
                    art_id = it.id.value,
                    name = it.name,
                    stock = it.availableStock
                )
            }
        )

    private fun writeAsJson(articleDao: InventoryDao): Either<FailureReason, Unit> =
        try {
            val content = mapper.writeValueAsString(articleDao)
            datasource.write(content)
        } catch (e: Exception) {
            Left(ArticlesSerializationError(e))
        }
}

private data class InventoryDao(
    val inventory: List<ArticleDao>
)

private data class ArticleDao(
    val art_id: Long,
    val name: String,
    val stock: Int
)