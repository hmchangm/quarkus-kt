package idv.brandy.service
import arrow.core.Either
import arrow.core.rightIfNotNull
import arrow.core.rightIfNull
import idv.brandy.model.Fruit
import idv.brandy.repository.FruitRepository
import java.lang.Exception
import java.lang.RuntimeException
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService (val fruitRepository: FruitRepository){

    fun findAll() : Either<RuntimeException, List<Fruit>> = Either.catch{
      fruitRepository.findAll().list<Fruit>()}.mapLeft { RuntimeException(it) }
}

object RepositoryError