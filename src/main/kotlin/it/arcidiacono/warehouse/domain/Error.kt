package it.arcidiacono.warehouse.domain

sealed class FailureReason

object ProductRepositoryError : FailureReason()
object ArticleRepositoryError : FailureReason()

data class NotEnoughQuantity(val sellableQuantity: Int) : FailureReason()
object NoMatchingProductFound : FailureReason()
object NoMatchingArticleFound : FailureReason()

data class ProductsDeserializationError(val throwable: Throwable) : FailureReason()
data class ArticlesDeserializationError(val throwable: Throwable) : FailureReason()
data class ArticlesSerializationError(val throwable: Throwable) : FailureReason()
data class DatasourceError(val throwable: Throwable) : FailureReason()
