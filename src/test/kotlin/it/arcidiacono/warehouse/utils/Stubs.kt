package it.arcidiacono.warehouse.utils

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.adapter.Datasource
import it.arcidiacono.warehouse.domain.DatasourceError
import it.arcidiacono.warehouse.domain.FailureReason

val inMemoryDatasource: (String) -> Datasource =
    { filePath: String ->
        object : Datasource {
            private var content: String = "initial"

            override fun read(): Either<FailureReason, String> {
                if (content == "initial") {
                    try {
                        content = javaClass.getResource(filePath).readText()
                    } catch (e: Exception) {
                        Left(DatasourceError(e))
                    }
                }

                return Right(content)
            }

            override fun write(content: String): Either<FailureReason, Unit> {
                this.content = content
                return Right(Unit)
            }
        }
    }