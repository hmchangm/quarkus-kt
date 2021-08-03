package idv.brandy.repository

import arrow.core.Option
import arrow.core.toOption
import arrow.fx.IO
import idv.brandy.model.Fruit
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitRepository: PanacheRepositoryBase<Fruit, String> {
    suspend fun findByUuid(uuid:String): Option<Fruit>  = find("uuid", uuid).firstResult<Fruit>().toOption()
    fun findByName(name: String) = find("name", name).firstResult<Fruit>()
}