package it.arcidiacono.warehouse.domain

sealed class FailureReason

object ProductRepositoryError : FailureReason()
object ArticleRepositoryError : FailureReason()