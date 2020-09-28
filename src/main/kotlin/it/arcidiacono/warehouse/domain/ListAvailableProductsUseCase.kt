package it.arcidiacono.warehouse.domain

import arrow.core.Either

interface ListAvailableProductsUseCase {
    fun execute(): Either<FailureReason, List<AvailableProduct>>
}

class ListAvailableProductsUseCaseImpl(
    private val warehouseRepository: WarehouseRepository
) : ListAvailableProductsUseCase {
    override fun execute(): Either<FailureReason, List<AvailableProduct>> =
        warehouseRepository.fetch()
            .map { products ->
                products.filter { it.sellableQuantity() > 0 }
                    .map {
                        AvailableProduct(
                            name = it.name,
                            price = it.price,
                            availableQuantity = it.sellableQuantity()
                        )
                    }
            }
}
