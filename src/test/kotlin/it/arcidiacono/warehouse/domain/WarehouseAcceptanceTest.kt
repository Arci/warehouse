package it.arcidiacono.warehouse.domain

import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.adapter.JsonArticlesRepository
import it.arcidiacono.warehouse.adapter.JsonProductsRepository
import it.arcidiacono.warehouse.adapter.WarehouseRepositoryImpl
import it.arcidiacono.warehouse.utils.Fixtures.A_PRICE
import it.arcidiacono.warehouse.utils.inMemoryDatasource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WarehouseAcceptanceTest {

    private lateinit var listAvailableProductsUseCase: ListAvailableProductsUseCase
    private lateinit var sellProductUseCase: SellProductUseCase

    @BeforeEach
    fun setUp() {
        val productsRepository = JsonProductsRepository(inMemoryDatasource("/data/products.json"))
        val articlesRepository = JsonArticlesRepository(inMemoryDatasource("/data/inventory.json"))
        val warehouseRepository = WarehouseRepositoryImpl(productsRepository, articlesRepository)
        sellProductUseCase = SellProductUseCaseImpl(warehouseRepository)
        listAvailableProductsUseCase = ListAvailableProductsUseCaseImpl(warehouseRepository)
    }

    @Test
    fun `list available products with their quantities`() {
        listAvailableProductsUseCase.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = "Dining Chair",
                    price = A_PRICE,
                    availableQuantity = 2
                ),
                AvailableProduct(
                    name = "Dinning Table",
                    price = A_PRICE,
                    availableQuantity = 1
                )
            )
        )
    }

    @Test
    fun `selling an item reduces the articles in the inventory`() {
        sellProductUseCase.execute("Dinning Table", 1).shouldBeRight(Unit)
        listAvailableProductsUseCase.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = "Dining Chair",
                    price = A_PRICE,
                    availableQuantity = 1
                )
            )
        )
    }

    @Test
    fun `selling twice an item that can only be sold once`() {
        sellProductUseCase.execute("Dinning Table", 1).shouldBeRight(Unit)
        sellProductUseCase.execute("Dinning Table", 1).shouldBeLeft(NotEnoughQuantity(0))
    }
}