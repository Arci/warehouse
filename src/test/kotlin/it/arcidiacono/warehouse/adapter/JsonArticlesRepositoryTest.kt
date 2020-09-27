package it.arcidiacono.warehouse.adapter

import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.Article
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import it.arcidiacono.warehouse.domain.Material
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
    fun `when file does not exists`() {
        jsonArticlesRepository = JsonArticlesRepository(inMemoryDatasource("wathever"))

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

        jsonArticlesRepository.update(
            billOfMaterials = listOf(
                Material(
                    articleId = ArticleIdentificationNumber(1),
                    requiredAmount = 1
                ),
                Material(
                    articleId = ArticleIdentificationNumber(2),
                    requiredAmount = 3
                )
            ),
            quantity = 2
        ).shouldBeRight(Unit)
        jsonArticlesRepository.fetch().shouldBeRight(
            listOf(
                Article(
                    id = ArticleIdentificationNumber(1),
                    name = "leg",
                    availableStock = 10
                ),
                Article(
                    id = ArticleIdentificationNumber(2),
                    name = "screw",
                    availableStock = 11
                )
            )
        )
    }
}