package it.arcidiacono.warehouse.domain

sealed class FailureReason

data class NotEnoughQuantity(val sellableQuantity: Int) : FailureReason()
data class NoMatchingProductFound(val product: String) : FailureReason()

sealed class RepositoryError : FailureReason()
data class ProductsDeserializationError(val throwable: Throwable) : RepositoryError()
data class ArticlesDeserializationError(val throwable: Throwable) : RepositoryError()
data class ArticlesSerializationError(val throwable: Throwable) : RepositoryError()
data class DatasourceError(val throwable: Throwable) : RepositoryError()
