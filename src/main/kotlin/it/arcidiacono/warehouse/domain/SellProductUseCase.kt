package it.arcidiacono.warehouse.domain

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative

typealias SellProduct = (productName: String, quantity: Int) -> Either<FailureReason, Unit>
typealias UpdateArticles = (id: ArticleIdentificationNumber, quantity: Int) -> Either<FailureReason, Unit>

class SellProductUseCase(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository,
    private val updateArticles: UpdateArticles
) : SellProduct {
    override fun invoke(productName: String, quantity: Int): Either<FailureReason, Unit> =
        Either.applicative<FailureReason>().mapN(
            productsRepository(),
            articlesRepository()
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
                            product.billOfMaterials.forEach { material ->
                                updateArticles(material.articleId, material.requiredAmount * quantity)
                            }

                            Right(Unit)
                        }
                    }
                )
        }

    private fun sellableQuantityFor(product: Product, articles: List<Article>): Int {
        val largestMaterial = product.billOfMaterials.maxByOrNull { material -> material.requiredAmount }!!
        val article = articles.find { it.id == largestMaterial.articleId }!!
        return article.availableStock / largestMaterial.requiredAmount
    }
}