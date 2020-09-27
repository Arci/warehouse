package it.arcidiacono.warehouse.utils

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.adapter.Datasource
import it.arcidiacono.warehouse.domain.*

val inMemoryDatasource: (String) -> Datasource = { filePath: String ->
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

val stubProductsRepositoryWith: (Either<FailureReason, List<Product>>) -> ProductsRepository = { result: Either<FailureReason, List<Product>> ->
    object : ProductsRepository {
        override fun fetch(): Either<FailureReason, List<Product>> = result
    }
}

val stubArticlesRepositoryWith: (Either<FailureReason, List<Article>>) -> ArticlesRepository = { result: Either<FailureReason, List<Article>> ->
    object : ArticlesRepository {
        override fun fetch(): Either<FailureReason, List<Article>> = result

        override fun update(billOfMaterials: List<Material>, quantity: Int): Either<FailureReason, Unit> = Right(Unit)
    }
}