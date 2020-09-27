package it.arcidiacono.warehouse.web

import it.arcidiacono.warehouse.domain.SellProductUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SellProductEndpoint(
    private val sellProductUseCase: SellProductUseCase
) {
    private val logger: Logger = LoggerFactory.getLogger(SellProductEndpoint::class.java)

    @PostMapping("products/sell")
    fun sellProduct(
        @RequestBody request: SellProductRequest
    ): ResponseEntity<Any> =
        sellProductUseCase.execute(request.name, request.quantity)
            .fold(
                { failureReason ->
                    logger.error("failed to sell product due to: $failureReason")
                    ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
                },
                { ResponseEntity(HttpStatus.NO_CONTENT) }
            )
}

data class SellProductRequest(
    val name: String,
    val quantity: Int
)
