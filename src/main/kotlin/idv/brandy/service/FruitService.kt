package idv.brandy.service
import arrow.core.Either
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService (val fruitRepository: FruitRepository){

    fun findAll() : Either<RuntimeException, List<Fruit>> = Either.catch{
      fruitRepository.findAll().list<Fruit>()}.mapLeft { RuntimeException(it) }
}