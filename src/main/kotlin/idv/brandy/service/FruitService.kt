package idv.brandy.service

import arrow.core.Either
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
}