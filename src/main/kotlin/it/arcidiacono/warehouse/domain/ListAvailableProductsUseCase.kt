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
                val availableFor = availableFor(product, articles)
                if (availableFor > 0) {
                    AvailableProduct(
                        product.name,
                        product.price,
                        availableFor
                    )
                } else {
                    null
                }
            }
        }.fix()

    private fun availableFor(product: Product, articles: List<Article>): Int {
        val map = product.billOfMaterials.map { material ->
            val article = articles.find { it.id == material.articleId }!!

            material.requiredAmount to article.availableStock
        }

        val map1 = map.map { (requiredAmount, availableAmount) ->
            availableAmount / requiredAmount
        }

        return map1.minOrNull()!!
    }
}
