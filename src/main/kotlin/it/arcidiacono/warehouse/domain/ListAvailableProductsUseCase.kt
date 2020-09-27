package it.arcidiacono.warehouse.domain

import arrow.core.Either

typealias ListAvailableProducts = () -> Either<FailureReason, List<Product>>
typealias ProductsRepository = () -> Either<FailureReason, List<Product>>

class ListAvailableProductsUseCase(
    private val productsRepository: ProductsRepository
) : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<Product>> =
        productsRepository().map { products ->
            products.filter { product ->
                product.articles.all { it.availableStock > 0 }
            }
        }
}
