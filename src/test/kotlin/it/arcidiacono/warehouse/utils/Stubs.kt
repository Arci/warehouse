package it.arcidiacono.warehouse.utils

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.adapter.Datasource
import it.arcidiacono.warehouse.domain.FailureReason
import it.arcidiacono.warehouse.domain.ThrowableFailure
import java.nio.charset.StandardCharsets

val inMemoryDatasource: (String) -> Datasource =
    { filePath: String ->
        object : Datasource {
            private var content: String = "initial"

            override fun read(): Either<FailureReason, String> {
                if (content == "initial") {
                    try {
                        content = FixtureLoader.readFile(filePath)
                    } catch (e: Exception) {
                        Left(ThrowableFailure(e))
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

object FixtureLoader {
    fun readFile(path: String): String = javaClass.getResource(path).readText(StandardCharsets.UTF_8)
}