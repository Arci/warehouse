package it.arcidiacono.warehouse.domain

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative

interface SellProductUseCase {
    fun execute(productName: String, quantity: Int): Either<FailureReason, Unit>
}

class SellProductUseCaseImpl(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : SellProductUseCase {
    override fun execute(productName: String, quantity: Int): Either<FailureReason, Unit> =
        Either.applicative<FailureReason>().mapN(
            productsRepository.fetch(),
            articlesRepository.fetch()
        ) { (products, articles) ->
            Pair(products, articles)
        }.flatMap { (products, articles) ->
            products.find { it.name == productName }.toOption()
                .fold(
                    { Left(NoMatchingProductFound) },
                    { product ->
                        val sellableQuantity = sellableQuantityFor(product, articles)
                        if (quantity > sellableQuantity) {
                            Left(NotEnoughQuantity(sellableQuantity))
                        } else {
                            articlesRepository.update(product.billOfMaterials, quantity)
                            Right(Unit)
                        }
                    }
                )
        }

    private fun sellableQuantityFor(product: Product, articles: List<Article>): Int =
        product.billOfMaterials.map { material ->
            val article = articles.find { it.id == material.articleId }!!
            material.requiredAmount to article.availableStock
        }.map { (requiredAmount, availableAmount) ->
            availableAmount / requiredAmount
        }.minOrNull()!!
}