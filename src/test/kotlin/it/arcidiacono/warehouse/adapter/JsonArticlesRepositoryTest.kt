package it.arcidiacono.warehouse.adapter

import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.Article
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import it.arcidiacono.warehouse.utils.inMemoryDatasource
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JsonArticlesRepositoryTest {

    private lateinit var jsonArticlesRepository: JsonArticlesRepository

    @Test
    fun `fetch happy path`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("/articles/inventory.json"))

        jsonArticlesRepository.fetch().shouldBeRight(
            listOf(
                Article(
                    id = ArticleIdentificationNumber(1),
                    name = "leg",
                    availableStock = 12
                ),
                Article(
                    id = ArticleIdentificationNumber(2),
                    name = "screw",
                    availableStock = 17
                )
            )
        )
    }

    @Test
    fun `when datasource fails`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("whatever"))

        assertTrue(jsonArticlesRepository.fetch().isLeft())
    }

    @Test
    fun `when json is not valid`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("invalid.json"))

        assertTrue(jsonArticlesRepository.fetch().isLeft())
    }

    @Test
    fun `when no article specified`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("/articles/noInventory.json"))

        jsonArticlesRepository.fetch().shouldBeRight(emptyList())
    }

    @Test
    fun `update happy path`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("/articles/inventory.json"))

        val toBeUpdated = listOf(
            Article(
                id = ArticleIdentificationNumber(1),
                name = "leg",
                availableStock = 6
            )
        )
        jsonArticlesRepository.update(toBeUpdated).shouldBeRight(Unit)
        jsonArticlesRepository.fetch().shouldBeRight(
            listOf(
                Article(
                    id = ArticleIdentificationNumber(1),
                    name = "leg",
                    availableStock = 6
                ),
                Article(
                    id = ArticleIdentificationNumber(2),
                    name = "screw",
                    availableStock = 17
                )
            )
        )
    }
}