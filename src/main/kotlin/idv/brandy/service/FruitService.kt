package idv.brandy.service

import arrow.core.Either
import arrow.core.flatMap
import idv.brandy.FruitError
import idv.brandy.FruitError.DatabaseProblem
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(val fruitRepository: FruitRepository) {
    fun findAll(): Either<DatabaseProblem, List<Fruit>> = Either.catch{fruitRepository.findAll().list<Fruit>()}.mapLeft { DatabaseProblem }

    fun save(fruit: Fruit): Either<DatabaseProblem, Fruit> =
        Either.catch { fruitRepository.persist(fruit);fruit }.mapLeft { DatabaseProblem }

    fun findByUuid(uuid: String): Either<FruitError, Fruit> = fruitRepository.findByUuid(uuid)

    fun delete(fruit: Fruit): Either<DatabaseProblem, Unit> = Either.catch { fruitRepository.delete(fruit) }.mapLeft { DatabaseProblem }

    fun modify(uuid: String, fruit: Fruit): Either<FruitError, Fruit> =
        findByUuid(uuid).map { it.copy(name = fruit.name) }.map { save(it) }.flatMap { x->x }

//        Either.catch {
//        when (val either = findByUuid(uuid)) {
//            is Either.Left -> throw RuntimeException("data store access error", either.value)
//            is Either.Right -> when (val opt = either.value) {
//                is None -> throw RuntimeException("no this fruit $uuid")
//                is Some -> opt.value.copy(name = fruit.name).let { save(it);it }
//            }
//        }
//    }
}