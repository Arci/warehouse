package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.Left
import arrow.core.flatMap
import arrow.core.toOption

interface SellProductUseCase {
    fun execute(productName: String, quantity: Int): Either<FailureReason, Unit>
}

class SellProductUseCaseImpl(
    private val warehouseRepository: WarehouseRepository
) : SellProductUseCase {
    override fun execute(productName: String, quantity: Int): Either<FailureReason, Unit> =
        warehouseRepository.fetch()
            .flatMap { products ->
                products.find { it.name == productName }.toOption()
                    .fold(
                        { Left(NoMatchingProductFound(productName)) },
                        { product ->
                            val sellableQuantity = product.sellableQuantity()
                            if (quantity > sellableQuantity) {
                                Left(NotEnoughQuantity(sellableQuantity))
                            } else {
                                warehouseRepository.sell(product, quantity)
                            }
                        }
                    )
            }
}

