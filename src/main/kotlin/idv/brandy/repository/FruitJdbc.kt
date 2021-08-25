package idv.brandy.repository

import arrow.core.Either
import com.vladsch.kotlin.jdbc.session
import com.vladsch.kotlin.jdbc.sqlQuery
import com.vladsch.kotlin.jdbc.using
import idv.brandy.FruitError
import idv.brandy.FruitError.DatabaseProblem
import idv.brandy.model.Fruit
import javax.enterprise.context.ApplicationScoped
import javax.sql.DataSource


@ApplicationScoped
class FruitJdbc(val dataSource: DataSource) {

    fun update(fruit: Fruit): Either<FruitError, Fruit> =
        Either.catch {
            using(session(dataSource)) { session ->
                session.update(
                    sqlQuery(
                        "update fruit set name=?,description=? where uuid=?",
                        fruit.name,
                        fruit.description,
                        fruit.uuid
                    )
                )
            }
            fruit
        }.mapLeft { DatabaseProblem(it) }

}