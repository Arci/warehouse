package it.arcidiacono.warehouse.domain

import arrow.core.Either

interface ArticlesRepository {
    fun fetch(): Either<FailureReason, List<Article>>
    fun update(articles: List<Article>): Either<FailureReason, Unit>
}