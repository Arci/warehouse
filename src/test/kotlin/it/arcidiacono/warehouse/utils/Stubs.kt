package it.arcidiacono.warehouse.utils

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.adapter.Datasource
import it.arcidiacono.warehouse.domain.DatasourceError
import it.arcidiacono.warehouse.domain.FailureReason
import it.arcidiacono.warehouse.domain.Product2
import it.arcidiacono.warehouse.domain.WarehouseRepository

val inMemoryDatasource: (String) -> Datasource =
    { filePath ->
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

val stubWarehouseRepositoryWith: (Either<FailureReason, List<Product2>>) -> WarehouseRepository =
    { result ->
        object : WarehouseRepository {
            override fun fetch(): Either<FailureReason, List<Product2>> = result

            override fun sell(product: Product2, quantity: Int): Either<FailureReason, Unit> = Right(Unit)
        }
    }