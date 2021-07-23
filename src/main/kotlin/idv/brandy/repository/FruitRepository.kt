package idv.brandy.repository

import idv.brandy.model.Fruit
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitRepository: PanacheRepositoryBase<Fruit, String> {
    fun findByName(name: String) = find("name", name).firstResult<Fruit>()
}