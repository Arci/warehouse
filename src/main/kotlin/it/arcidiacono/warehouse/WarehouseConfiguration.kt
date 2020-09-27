package it.arcidiacono.warehouse

import arrow.core.Right
import it.arcidiacono.warehouse.adapter.FileArticlesRepository
import it.arcidiacono.warehouse.adapter.FileProductsRepository
import it.arcidiacono.warehouse.domain.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WarehouseConfiguration {

    @Bean
    fun listAvailableProductsUseCase(
        productsRepository: ProductsRepository,
        articlesRepository: ArticlesRepository
    ): ListAvailableProductsUseCase =
        ListAvailableProductsUseCaseImpl(
            productsRepository,
            articlesRepository
        )

    @Bean
    fun sellProductUseCase(
        productsRepository: ProductsRepository,
        articlesRepository: ArticlesRepository
    ): SellProductUseCase =
        SellProductUseCaseImpl(
            productsRepository,
            articlesRepository,
            { _: ArticleIdentificationNumber, _: Int -> Right(Unit) }
        )

    @Bean
    fun articlesRepository(): ArticlesRepository = FileArticlesRepository("/data/inventory.json")

    @Bean
    fun productsRepository(): ProductsRepository = FileProductsRepository("/data/products.json")
}