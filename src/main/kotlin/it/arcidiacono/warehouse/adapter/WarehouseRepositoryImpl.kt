package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.fix
import it.arcidiacono.warehouse.domain.FailureReason
import it.arcidiacono.warehouse.domain.Material
import it.arcidiacono.warehouse.domain.Product
import it.arcidiacono.warehouse.domain.WarehouseRepository

class WarehouseRepositoryImpl(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : WarehouseRepository {
    override fun fetch(): Either<FailureReason, List<Product>> =
        Either.applicative<FailureReason>().mapN(
            productsRepository.fetch(),
            articlesRepository.fetch()
        ) { (productsDto, articles) ->
            productsDto.map { productDto ->
                val materials = productDto.billOfMaterials.map { materialDto ->
                    val article = articles.find { it.id == materialDto.articleId }!!
                    Material(
                        article,
                        materialDto.requiredAmount
                    )
                }
                Product(
                    productDto.name,
                    productDto.price,
                    materials,
                )
            }
        }.fix()

    override fun sell(product: Product, quantity: Int): Either<FailureReason, Unit> {
        val updatedArticles = product.billOfMaterials.map { material ->
            material.article.reduceAvailabilityBy(material.requiredAmount * quantity)
        }

        return articlesRepository.update(updatedArticles)
    }
}

