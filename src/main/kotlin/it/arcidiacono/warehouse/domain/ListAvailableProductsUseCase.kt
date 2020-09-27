package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.fix

typealias ListAvailableProducts = () -> Either<FailureReason, List<AvailableProduct>>
typealias ProductsRepository = () -> Either<FailureReason, List<Product>>
typealias ArticlesRepository = () -> Either<FailureReason, List<Article>>

class ListAvailableProductsUseCase(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : ListAvailableProducts {
    override fun invoke(): Either<FailureReason, List<AvailableProduct>> =
        Either.applicative<FailureReason>().mapN(
            productsRepository(),
            articlesRepository()
        ) { (products, articles) ->
            listOf<AvailableProduct>()
        }.fix()
}
