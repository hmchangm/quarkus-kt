package idv.brandy.repository

import arrow.core.Either
import idv.brandy.FruitError
import idv.brandy.FruitError.DatabaseProblem
import idv.brandy.model.Fruit
import org.sql2o.Query
import org.sql2o.Sql2o
import javax.enterprise.context.ApplicationScoped
import javax.sql.DataSource


@ApplicationScoped
class FruitJdbc(val datasource: DataSource) {
    var sql2o: Sql2o = Sql2o(datasource)

    fun update(fruit: Fruit): Either<FruitError, Fruit> =
        Either.catch {
            sql2o.open().use { conn ->
                conn.createQuery(
                    "update fruit set name=:name,description=:description where uuid=:uuid"
                ).bind(fruit).executeUpdate()
            }
            fruit
        }.mapLeft { DatabaseProblem(it) }

}