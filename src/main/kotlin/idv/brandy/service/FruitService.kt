package idv.brandy.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.flatten
import idv.brandy.FruitError
import idv.brandy.FruitError.DatabaseProblem
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitJdbc
import idv.brandy.repository.FruitRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(val fruitRepository: FruitRepository, val fruitJdbc: FruitJdbc) {
    fun findAll(): Either<DatabaseProblem, List<Fruit>> =
        Either.catch { fruitRepository.findAll().list<Fruit>() }.mapLeft { DatabaseProblem(it) }

    fun save(fruit: Fruit): Either<DatabaseProblem, Fruit> =
        Either.catch { fruitRepository.persist(fruit);fruit }.mapLeft { DatabaseProblem(it) }

    fun findByUuid(uuid: String): Either<FruitError, Fruit> = fruitRepository.findByUuid(uuid)

    fun delete(fruit: Fruit): Either<DatabaseProblem, Unit> =
        Either.catch { fruitRepository.delete(fruit) }.mapLeft { DatabaseProblem(it) }

    fun modify(uuid: String, fruit: Fruit): Either<FruitError, Fruit> =
        findByUuid(uuid).map { it.copy(name = fruit.name, description = fruit.description) }
            .map { fruitJdbc.update(it) }.flatten()

    fun deleteByUuid(uuid: String): Either<FruitError, Unit> =
        findByUuid(uuid).map { delete(it) }

}