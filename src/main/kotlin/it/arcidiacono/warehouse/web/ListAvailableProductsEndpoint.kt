package it.arcidiacono.warehouse.web

import it.arcidiacono.warehouse.domain.ListAvailableProductsUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ListAvailableProductsEndpoint(
    private val listAvailableProductsUseCase: ListAvailableProductsUseCase
) {
    private val logger: Logger = LoggerFactory.getLogger(ListAvailableProductsEndpoint::class.java)

    @GetMapping("products/available")
    fun availableProducts(): ResponseEntity<Any> =
        listAvailableProductsUseCase.execute()
            .fold(
                { failureReason ->
                    logger.error("failed to retrieve available products due to: $failureReason")
                    ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
                },
                { availableProducts -> ResponseEntity.ok(availableProducts) }
            )
}
