package idv.brandy.service

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(val fruitRepository: FruitRepository) {

    fun findAll() = Either.catch {
        fruitRepository.findAll().list<Fruit>()
    }.mapLeft { it }

    fun save(fruit: Fruit) = Either.catch {
        fruitRepository.persist(fruit)
    }.mapLeft { it }

    fun findByUuid(uuid: String) = Either.catch {
        fruitRepository.findByUuid(uuid)
    }.mapLeft { it }

    fun delete(fruit: Fruit) = Either.catch {
        fruitRepository.delete(fruit)
    }.mapLeft { it }

    fun modify(uuid: String, fruit: Fruit) = Either.catch {
        when (val either = findByUuid(uuid)) {
            is Either.Left -> throw RuntimeException("data store access error", either.value)
            is Either.Right -> when (val opt = either.value) {
                is None -> throw RuntimeException("no this fruit $uuid")
                is Some -> opt.value.copy(name = fruit.name).let { save(it);it }
            }
        }
    }
}