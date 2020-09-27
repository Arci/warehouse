package it.arcidiacono.warehouse

import it.arcidiacono.warehouse.adapter.FileDatasource
import it.arcidiacono.warehouse.adapter.JsonArticlesRepository
import it.arcidiacono.warehouse.adapter.JsonProductsRepository
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
            articlesRepository
        )

    @Bean
    fun articlesRepository(): ArticlesRepository = JsonArticlesRepository(FileDatasource("/data/inventory.json"))

    @Bean
    fun productsRepository(): ProductsRepository = JsonProductsRepository(FileDatasource("/data/products.json"))
}