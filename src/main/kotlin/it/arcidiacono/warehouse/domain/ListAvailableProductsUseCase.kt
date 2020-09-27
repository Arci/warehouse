package it.arcidiacono.warehouse.domain

import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.fix

interface ListAvailableProductsUseCase {
    fun execute(): Either<FailureReason, List<AvailableProduct>>
}

class ListAvailableProductsUseCaseImpl(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : ListAvailableProductsUseCase {
    override fun execute(): Either<FailureReason, List<AvailableProduct>> =
        Either.applicative<FailureReason>().mapN(
            productsRepository(),
            articlesRepository()
        ) { (products, articles) ->
            products.mapNotNull { product ->
                val sellableQuantity = sellableQuantityFor(product, articles)
                if (sellableQuantity > 0) {
                    AvailableProduct(
                        name = product.name,
                        price = product.price,
                        availableQuantity = sellableQuantity
                    )
                } else {
                    null
                }
            }
        }.fix()

    private fun sellableQuantityFor(product: Product, articles: List<Article>): Int =
        product.billOfMaterials.map { material ->
            val article = articles.find { it.id == material.articleId }!!
            material.requiredAmount to article.availableStock
        }.map { (requiredAmount, availableAmount) ->
            availableAmount / requiredAmount
        }.minOrNull()!!
}
