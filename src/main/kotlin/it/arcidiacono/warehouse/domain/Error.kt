package it.arcidiacono.warehouse.domain

sealed class FailureReason

object ProductRepositoryError : FailureReason()
object ArticleRepositoryError : FailureReason()

data class NotEnoughQuantity(val sellableQuantity: Int) : FailureReason()
object NoMatchingProductFound : FailureReason()
