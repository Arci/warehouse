package it.arcidiacono.warehouse.domain

sealed class FailureReason

object ProductRepositoryError : FailureReason()
object ArticleRepositoryError : FailureReason()

data class NotEnoughQuantity(val sellableQuantity: Int) : FailureReason()
data class NoMatchingProductFound(val product: String) : FailureReason()

data class ProductsDeserializationError(val throwable: Throwable) : FailureReason()
data class ArticlesDeserializationError(val throwable: Throwable) : FailureReason()
data class ArticlesSerializationError(val throwable: Throwable) : FailureReason()
data class DatasourceError(val throwable: Throwable) : FailureReason()
