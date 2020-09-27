package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import it.arcidiacono.warehouse.utils.Fixtures.ANOTHER_PRODUCT
import it.arcidiacono.warehouse.utils.Fixtures.A_PRODUCT
import it.arcidiacono.warehouse.utils.stubWarehouseRepositoryFetchWith
import org.junit.jupiter.api.Test

class SellProductUseCaseImplTest {

    private lateinit var sellProductsUseCaseImpl: SellProductUseCaseImpl

    @Test
    fun `happy path`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute(A_PRODUCT.name, 1).shouldBeRight()
    }

    @Test
    fun `try to sell too many items of a product`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute(A_PRODUCT.name, 4).shouldBeLeft(NotEnoughQuantity(1))
    }

    @Test
    fun `try to sell a non existent product`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf(A_PRODUCT, ANOTHER_PRODUCT)))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute("aNonExistingProduct", 1).shouldBeLeft(NoMatchingProductFound("aNonExistingProduct"))
    }

    @Test
    fun `when no product is available`() {
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Right(listOf()))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(NoMatchingProductFound("whatever"))
    }

    @Test
    fun `when warehouse fetch fails`() {
        val expectedError = DatasourceError(RuntimeException("something failed :("))
        val warehouseRepository = stubWarehouseRepositoryFetchWith(Left(expectedError))
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute("whatever", 1).shouldBeLeft(expectedError)
    }

    @Test
    fun `when warehouse sell fails`() {
        val expectedError = DatasourceError(RuntimeException("something failed :("))
        val warehouseRepository = object : WarehouseRepository {
            override fun fetch(): Either<FailureReason, List<Product>> = Right(listOf(A_PRODUCT))

            override fun sell(product: Product, quantity: Int): Either<FailureReason, Unit> = Left(expectedError)
        }
        sellProductsUseCaseImpl = SellProductUseCaseImpl(warehouseRepository)

        sellProductsUseCaseImpl.execute(A_PRODUCT.name, 1).shouldBeLeft(expectedError)
    }
}