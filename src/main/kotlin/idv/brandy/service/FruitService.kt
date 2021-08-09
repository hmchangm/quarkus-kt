package idv.brandy.service

import arrow.core.*
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(val fruitRepository: FruitRepository) {
    // TODO - mutable list?
    fun findAll(): Either<Throwable, MutableList<Fruit>> = Either.catch { fruitRepository.findAll().list() }

    fun save(fruit: Fruit): Either<Throwable, Unit> = Either.catch { fruitRepository.persist(fruit) }

    fun findByUuid(uuid: String): Either<Throwable, Option<Fruit>> = fruitRepository.findByUuid(uuid)

    fun delete(fruit: Fruit): Either<Throwable, Unit> = Either.catch { fruitRepository.delete(fruit) }

    fun modify(uuid: String, fruit: Fruit): Either<Throwable, Fruit> =
        findByUuid(uuid).flatMap { x ->
            x.map { it.copy(name = fruit.name) }.let {
                when (val f = it) {
                    is Some -> f.value.let { v -> save(v).map { v } }
                    is None -> Either.Left(RuntimeException("No fruit with UUID: $uuid found"))
                }
            }
        }
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