package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.arcidiacono.warehouse.domain.*

class FileArticlesRepository(
    private val articlesFile: String
) : ArticlesRepository {
    private val mapper = jacksonObjectMapper()

    override fun fetch(): Either<FailureReason, List<Article>> =
        try {
            val fileContent = FixtureLoader.readFile(articlesFile)
            val articlesDao = mapper.readValue<ArticlesDao>(fileContent)
            Right(articlesDao.toDomain())
        } catch (e: Exception) {
            Left(ThrowableFailure(e))
        }

    override fun update(id: ArticleIdentificationNumber, quantity: Int): Either<FailureReason, Unit> {
        TODO("Not yet implemented")
    }

    private fun ArticlesDao.toDomain(): List<Article> =
        this.inventory.map {
            Article(
                id = ArticleIdentificationNumber(it.art_id),
                name = it.name,
                availableStock = it.stock
            )
        }
}

private data class ArticlesDao(
    val inventory: List<ArticleDao>
)

private data class ArticleDao(
    val art_id: Long,
    val name: String,
    val stock: Int
)

