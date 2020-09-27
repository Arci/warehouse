package it.arcidiacono.warehouse.domain

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_UNAVAILABLE_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.AN_UNAVAILABLE_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT
import it.arcidiacono.warehouse.utils.stubWarehouseRepositoryFetchWith
import org.junit.jupiter.api.Test

class ListAvailableProductsUseCaseImplTest {

    private lateinit var listAvailableProductsUseCaseImpl: ListAvailableProductsUseCaseImpl

    @Test
    fun `list available products with their quantities`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(warehouseRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = A_PRODUCT.name,
                    price = A_PRODUCT.price,
                    availableQuantity = 1
                ),
                AvailableProduct(
                    name = ANOTHER_PRODUCT.name,
                    price = ANOTHER_PRODUCT.price,
                    availableQuantity = 3
                )
            )
        )
    }

    @Test
    fun `when a product is not available is not listed`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(
            Right(
                listOf(
                    A_PRODUCT,
                    AN_UNAVAILABLE_PRODUCT,
                    ANOTHER_UNAVAILABLE_PRODUCT
                )
            )
        )
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(warehouseRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(
            listOf(
                AvailableProduct(
                    name = A_PRODUCT.name,
                    price = A_PRODUCT.price,
                    availableQuantity = 1
                )
            )
        )
    }

    @Test
    fun `when no product is available`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf()))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(warehouseRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeRight(listOf())
    }

    @Test
    fun `when warehouse repository fails`() {
        val expectedError = DatasourceError(RuntimeException("something failed :("))
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Left(expectedError))
        listAvailableProductsUseCaseImpl = ListAvailableProductsUseCaseImpl(warehouseRepository)

        listAvailableProductsUseCaseImpl.execute().shouldBeLeft(expectedError)
    }
}