package it.arcidiacono.warehouse.utils

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import it.arcidiacono.warehouse.adapter.ArticlesRepository
import it.arcidiacono.warehouse.adapter.Datasource
import it.arcidiacono.warehouse.adapter.ProductDto
import it.arcidiacono.warehouse.adapter.ProductsRepository
import it.arcidiacono.warehouse.domain.*

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

val stubWarehouseRepositoryFetchWith: (Either<FailureReason, List<Product>>) -> WarehouseRepository =
    { result ->
        object : WarehouseRepository {
            override fun fetch(): Either<FailureReason, List<Product>> = result

            override fun sell(product: Product, quantity: Int): Either<FailureReason, Unit> = Right(Unit)
        }
    }

val stubProductsRepositoryWith: (Either<FailureReason, List<ProductDto>>) -> ProductsRepository =
    { result ->
        object : ProductsRepository {
            override fun fetch(): Either<FailureReason, List<ProductDto>> = result
        }
    }

val stubArticlesRepositoryWith: (Either<FailureReason, List<Article>>) -> ArticlesRepository =
    { result ->
        object : ArticlesRepository {
            override fun fetch(): Either<FailureReason, List<Article>> = result

            override fun update(articles: List<Article>): Either<FailureReason, Unit> = Right(Unit)
        }
    }