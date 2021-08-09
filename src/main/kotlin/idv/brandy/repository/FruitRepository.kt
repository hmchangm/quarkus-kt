package idv.brandy.repository

import arrow.core.Either
import arrow.core.Option
import idv.brandy.model.Fruit
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitRepository : PanacheRepositoryBase<Fruit, String> {
    fun findByUuid(uuid: String): Either<Throwable, Option<Fruit>> =
        Either.catch {
            Option.fromNullable(
                find("uuid", uuid)
                    .firstResultOptional<Fruit>().orElse(null)
            )
        }

    fun findByName(name: String) = find("name", name).firstResult<Fruit>()
}
