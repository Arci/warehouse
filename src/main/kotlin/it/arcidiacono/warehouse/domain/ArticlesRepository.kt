package it.arcidiacono.warehouse.domain

import arrow.core.Either

interface ArticlesRepository {
    fun fetch(): Either<FailureReason, List<Article>>
    fun update(billOfMaterials: List<Material>, quantity: Int): Either<FailureReason, Unit>
}