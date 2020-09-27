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

    private fun sellableQuantityFor(product: Product, articles: List<Article>): Int {
        val largestMaterial = product.billOfMaterials.maxByOrNull { material -> material.requiredAmount }!!
        val article = articles.find { it.id == largestMaterial.articleId }!!
        return article.availableStock / largestMaterial.requiredAmount
    }
}
