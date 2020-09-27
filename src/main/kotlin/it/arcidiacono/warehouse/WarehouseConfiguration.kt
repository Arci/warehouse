package it.arcidiacono.warehouse

import it.arcidiacono.warehouse.adapter.FileArticlesRepository
import it.arcidiacono.warehouse.adapter.FileProductsRepository
import it.arcidiacono.warehouse.domain.ListAvailableProductsUseCase
import it.arcidiacono.warehouse.domain.ListAvailableProductsUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WarehouseConfiguration {

    @Bean
    fun listAvailableProductsUseCase(): ListAvailableProductsUseCase =
        ListAvailableProductsUseCaseImpl(
            FileProductsRepository("/data/products.json"),
            FileArticlesRepository("/data/inventory.json")
        )
}