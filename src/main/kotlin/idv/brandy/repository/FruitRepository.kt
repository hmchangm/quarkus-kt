package idv.brandy.repository

import arrow.core.Either
import idv.brandy.FruitError
import idv.brandy.model.Fruit
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitRepository : PanacheRepositoryBase<Fruit, String> {
    fun findByUuid(uuid: String): Either<FruitError, Fruit> {
        runCatching { find("uuid", uuid).firstResultOptional<Fruit>() }.fold(
            {
                return when (it.isPresent) {
                    true -> Either.Right(it.get())
                    false -> Either.Left(FruitError.NoThisFruit(uuid))
                }
            }, { return Either.Left(FruitError.DatabaseProblem(it)) }
        )
    }

}
