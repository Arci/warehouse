package it.arcidiacono.warehouse.domain

sealed class FailureReason

object GenericFailure : FailureReason()