package idv.brandy.repository

import arrow.core.Option
import idv.brandy.model.Fruit
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitRepository : PanacheRepositoryBase<Fruit, String> {
    fun findByUuid(uuid: String): Option<Fruit> =
        Option.fromNullable(
            find("uuid", uuid)
                .firstResultOptional<Fruit>().orElse(null)
        )

    fun findByName(name: String) = find("name", name).firstResult<Fruit>()
}