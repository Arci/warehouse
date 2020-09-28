package it.arcidiacono.warehouse.adapter

import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.fix
import it.arcidiacono.warehouse.domain.Material
import it.arcidiacono.warehouse.domain.Product
import it.arcidiacono.warehouse.domain.RepositoryError
import it.arcidiacono.warehouse.domain.WarehouseRepository

class WarehouseRepositoryImpl(
    private val productsRepository: ProductsRepository,
    private val articlesRepository: ArticlesRepository
) : WarehouseRepository {
    override fun fetch(): Either<RepositoryError, List<Product>> =
        Either.applicative<RepositoryError>().mapN(
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

    override fun sell(product: Product, quantity: Int): Either<RepositoryError, Unit> {
        val updatedArticles = product.billOfMaterials.map {
            it.article.reduceAvailabilityBy(it.requiredAmount * quantity)
        }
        return articlesRepository.update(updatedArticles)
    }
}

