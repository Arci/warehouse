package it.arcidiacono.warehouse.domain

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative

interface SellProductUseCase {
    fun execute(productName: String, quantity: Int): Either<FailureReason, Unit>
}

class SellProductUseCaseImpl(
    private val warehouseRepository: WarehouseRepository
) : SellProductUseCase {
    override fun execute(productName: String, quantity: Int): Either<FailureReason, Unit> =
        warehouseRepository.fetch().flatMap { products ->
            products.find { it.name == productName }.toOption()
                .fold(
                    { Left(NoMatchingProductFound(productName)) },
                    { product ->
                        val sellableQuantity = product.sellableQuantity()
                        if (quantity > sellableQuantity) {
                            Left(NotEnoughQuantity(sellableQuantity))
                        } else {
                            warehouseRepository.sell(product, quantity)
                            Right(Unit)
                        }
                    }
                )
        }
}

interface WarehouseRepository {
    fun fetch(): Either<FailureReason, List<Product2>>
    fun sell(product: Product2, quantity: Int): Either<FailureReason, Unit>
}

class WarehouseRepositoryImpl(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : WarehouseRepository {
    override fun fetch(): Either<FailureReason, List<Product2>> =
        Either.applicative<FailureReason>().mapN(
            productsRepository.fetch(),
            articlesRepository.fetch()
        ) { (products, articles) ->
            products.map { product ->
                val materials = product.billOfMaterials.map { material ->
                    val article = articles.find { it.id == material.articleId }!!
                    Material2(
                        article,
                        material.requiredAmount
                    )
                }
                Product2(
                    product.name,
                    product.price,
                    materials,
                )
            }
        }.fix()

    override fun sell(product: Product2, quantity: Int): Either<FailureReason, Unit> {
        product.billOfMaterials.map { material ->
            material.article.reduceAvailabilityBy(material.requiredAmount * quantity)
        }

        val articles = product.billOfMaterials.map { it.article }
        return articlesRepository.update(articles)
    }
}