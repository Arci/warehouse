package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.Right

typealias ListAvailableProducts = () -> Either<FailureReason, List<AvailableProduct>>
typealias ProductsRepository = () -> Either<FailureReason, List<Product>>
typealias ArticlesRepository = () -> Either<FailureReason, List<Article>>

class ListAvailableProductsUseCase(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository = { Right(emptyList()) }
) : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<AvailableProduct>> = TODO()
}
