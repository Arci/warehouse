package it.arcidiacono.warehouse.adapter

import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.domain.Article
import it.arcidiacono.warehouse.domain.ArticleIdentificationNumber
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FileArticlesRepositoryTest {

    private lateinit var fileArticlesRepository: FileArticlesRepository

    @Test
    fun `happy path`() {
        fileArticlesRepository = FileArticlesRepository("/articles/inventory.json")

        fileArticlesRepository.fetch().shouldBeRight(
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
        fileArticlesRepository = FileArticlesRepository("wathever")

        assertTrue(fileArticlesRepository.fetch().isLeft())
    }

    @Test
    fun `when no article specified`() {
        fileArticlesRepository = FileArticlesRepository("/articles/noInventory.json")

        fileArticlesRepository.fetch().shouldBeRight(emptyList())
    }
}