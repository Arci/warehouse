package it.arcidiacono.warehouse

import it.arcidiacono.warehouse.adapter.FileDatasource
import it.arcidiacono.warehouse.adapter.JsonArticlesRepository
import it.arcidiacono.warehouse.adapter.JsonProductsRepository
import it.arcidiacono.warehouse.adapter.WarehouseRepositoryImpl
import it.arcidiacono.warehouse.domain.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WarehouseConfiguration {

    @Bean
    fun listAvailableProductsUseCase(
        warehouseRepository: WarehouseRepository
    ): ListAvailableProductsUseCase =
        ListAvailableProductsUseCaseImpl(warehouseRepository)

    @Bean
    fun sellProductUseCase(
        warehouseRepository: WarehouseRepository
    ): SellProductUseCase =
        SellProductUseCaseImpl(warehouseRepository)

    @Bean
    fun warehouseRepository(): WarehouseRepository =
        WarehouseRepositoryImpl(
            JsonProductsRepository(FileDatasource("/data/products.json")),
            JsonArticlesRepository(FileDatasource("/data/inventory.json"))
        )
}